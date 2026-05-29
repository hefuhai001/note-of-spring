# Spring Peak Clipping - 活动峰值削峰方案

> 将活动数据提前预热到 Redis，解决活动上架瞬间数据库被击穿的问题

## 技术栈

| 组件 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 4.0.4 | 基础框架 |
| JDK | 25 | 运行环境 |
| PostgreSQL | 17 | 主数据存储 |
| Redis | 7 | 活动缓存层 |

## 核心依赖说明

### Spring Data JPA
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```

**为什么选它而不是 MyBatis：**
- 本项目查询简单，JPA 的 `@Query` 注解够用
- `findByStatus`、`findById` 这种方法命名约定能少写 80% 的 SQL
- Hibernate 自动建表省去维护 DDL 的时间

**实际使用：**
```java
// 方法名即 SQL，零 XML 配置
List<Activity> findByStatus(ActivityStatus status);

// 复杂查询用 @Query
@Query("SELECT a FROM Activity a WHERE a.status IN :statuses AND a.isActive = true")
List<Activity> findByStatusInAndIsActiveTrue(@Param("statuses") List<ActivityStatus> statuses);
```

### Spring Data Redis (Lettuce)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

**Lettuce vs Jedis 选择理由：**
- Lettuce 是线程安全的，单连接多路复用（Netty 实现）
- Jedis 是直连模式，高并发要配连接池
- Spring Boot 2.x 默认就是 Lettuce，官方推荐

**序列化选择：**
```java
// 用 GenericJackson2JsonRedisSerializer 而不是 Java 序列化
// 理由：
// 1. Java 序列化体积大（3-5倍），Redis 内存贵
// 2. 可读性好，用 redis-cli 能直接看内容
// 3. 跨语言兼容，Python/Go 都能解析 JSON

GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
template.setValueSerializer(jsonSerializer);
```

**缓存 Key 设计：**
```
activity:cache:1           # Hash 结构存储活动详情
activity:cache:status:not_started   # Set 存储该状态的活动ID集合
```

**TTL 设置：** 60分钟过期，配合定时任务刷新，防止缓存雪崩时全部同时失效。

### Jackson (databind + jsr310)
```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
</dependency>
```

**jsr310 干什么的：**
- 支持 Java 8 时间类型（`LocalDateTime`、`Instant`）的 JSON 序列化
- 没有它会报错：`NoClassDefFoundError: TypeResolverBuilder`
- 自动将 `2026-03-22T21:00:00` 和 Java 对象互转

## 预热策略详解

### 三层预热机制

```
时间线 ──────────────────────────────────────────────────────►

应用启动          每5分钟         每1分钟        每5分钟
   │                │              │             │
   ▼                ▼              ▼             ▼
┌─────────┐   ┌──────────┐   ┌──────────┐   ┌──────────┐
│ 全量预热 │   │ 定时预热 │   │ 临门一脚 │   │ 缓存刷新 │
│ 所有活跃│   │ 未来30分 │   │ 未来5分  │   │ 同步库存 │
│ 活动    │   │ 钟内开始 │   │ 钟内开始 │   │ 重置TTL  │
└─────────┘   └──────────┘   └──────────┘   └──────────┘
```

| 层级 | Cron/FixedRate | 扫描范围 | 目的 |
|------|---------------|---------|------|
| 启动预热 | CommandLineRunner | 所有 isActive=true 活动 | 冷启动快速就绪 |
| 定时预热 | `0 */5 * * * *` | startTime 在未来30分钟内 | 批量提前加载 |
| 即将开始 | fixedRate=60000ms | startTime 在未来5分钟内 | 确保不遗漏 |
| 全量刷新 | fixedRate=300000ms | 所有 NOT_STARTED + IN_PROGRESS | 数据一致性 |

### 为什么这样设计

**问题场景：**
```
活动 20:00 开始，用户 19:59:59 开始疯狂刷新
如果没有预热 → 请求直接打数据库 → 数据库 CPU 100% → 全站崩溃
有预热 → 请求打 Redis → QPS 10万+ 无压力
```

**分层原因：**
1. **5分钟一次定时扫描**：避免每分钟都全表扫描，节省 DB 资源
2. **1分钟检查即将开始**：活动开始前是访问高峰，必须高频兜底
3. **5分钟全量刷新**：库存会变（有人下单），需要同步到 Redis

## 项目结构

```
src/main/java/com/example/api/
├── config/
│   ├── ActivityCacheProperties.java    # 缓存配置属性绑定
│   ├── ActivityCacheInitializer.java    # 启动时预热入口
│   └── RedisConfig.java                # RedisTemplate + CacheManager
├── entity/
│   ├── Activity.java                   # 活动实体（JPA）
│   └── ActivityStatus.java             # 状态枚举
├── repository/
│   └── ActivityRepository.java         # 数据访问层
├── service/
│   └── ActivityCacheService.java       # 核心缓存逻辑 ★
├── scheduler/
│   └── ActivityPreloadScheduler.java   # 定时任务调度器 ★
└── controller/
    └── ActivityController.java         # REST API
```

## 快速启动

### 方式一：Docker 一键启动（推荐）

```bash
# 启动 PostgreSQL + Redis + 管理界面
docker-compose up -d

# 启动应用（另开终端）
mvn spring-boot:run
```

### 方式二：本地开发

**前置条件：**
- PostgreSQL 17，数据库 `db_activity`，用户 `root`
- Redis 7，密码 `123456`

```bash
mvn spring-boot:run
```

## API 接口

```bash
# 创建活动并自动缓存
POST /api/activities
Content-Type: application/json

{
  "name": "双十一大促",
  "status": "not_started",
  "startTime": "2026-11-11T00:00:00",
  "endTime": "2026-11-12T00:00:00",
  "totalStock": 1000,
  "availableStock": 1000,
  "isActive": true
}

# 获取活动（优先读 Redis）
GET /api/activities/{id}

# 更新活动状态（自动更新缓存索引）
PUT /api/activities/{id}/status?status=in_progress

# 手动触发预热
POST /api/activities/preload

# 清空所有缓存
DELETE /api/activities/cache
```

## 配置项

```yaml
activity:
  cache:
    preload:
      enabled: true                    # 开关，false 则禁用所有定时任务
      cron: "0 */5 * * * *"           # 定时预热 cron 表达式
    key-prefix: "activity:cache:"      # Redis key 前缀，避免冲突
    ttl-minutes: 60                    # 缓存过期时间
```

## 性能对比

| 场景 | 直接查数据库 | Redis 缓存 |
|------|------------|-----------|
| 单次查询耗时 | 10-50ms | 0.1-1ms |
| 并发能力 | ~1000 QPS | ~100000 QPS |
| 活动开始瞬间 | ❌ 容易宕机 | ✅ 稳如老狗 |

## 注意事项

1. **缓存和数据库的一致性**：本项目采用「定时刷新」策略，适合对一致性要求不极高的场景（秒级延迟可接受）
2. **缓存击穿防护**：单个活动缓存失效时，大量请求会穿透到 DB，可考虑加分布式锁
3. **内存估算**：单个活动对象约 500B，10万个活动 ≈ 50MB Redis 内存
