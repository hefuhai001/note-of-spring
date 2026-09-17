# Spring Boolean Filter

基于 Spring Boot 4 + Gradle + MyBatis-Plus 的商品订单信息查询**三级缓存**示例项目。

## 架构概览

```
请求 ──→ ① 布隆过滤器(内存) ──→ ② Redis(缓存) ──→ ③ MySQL(数据库)
          纳秒级拦截              微秒级读取             毫秒级查询
         不存在直接返回            命中返回JSON           未命中查DB并回填
```

| 层级 | 存储 | 耗时 | 作用 |
|------|------|------|------|
| **L1** | BloomFilter（内存） | 纳秒级 | 拦截一定不存在的请求，零成本 |
| **L2** | Redis | 微秒级 | 缓存热点数据，减少 DB 压力 |
| **L3** | MySQL | 毫秒级 | 持久化存储，最终数据源 |

## Gradle 命令行

Gradle 使用项目根目录下的 `gradlew`（Windows 用 `gradlew.bat`）来执行构建任务，无需全局安装 Gradle。

### 常用命令

```bash
# 编译项目
./gradlew build                # Windows: gradlew.bat build

# 编译并跳过测试
./gradlew build -x test

# 运行测试
./gradlew test

# 运行 Spring Boot 应用
./gradlew bootRun

# 清理构建产物
./gradlew clean

# 查看所有可用任务
./gradlew tasks

# 查看依赖树（排查依赖冲突）
./gradlew dependencies

# 刷新依赖
./gradlew build --refresh-dependencies

# 生成 IDEA 项目文件
./gradlew idea

# 多模块项目：指定子模块执行
./gradlew :submodule:build
```

### Gradle Wrapper

项目自带 `gradle-wrapper.properties`，指定了 Gradle 版本。首次运行 `gradlew` 时会自动下载对应版本的 Gradle，确保团队使用一致的构建工具版本。

---

## 核心机制详解

### 一、启动预加载

应用启动时，从数据库一次性加载所有有效订单到 L1 和 L2：

```
@PostConstruct init()
  │
  ├── SELECT * FROM product_order WHERE deleted = 0   ← 从 DB 加载全部订单
  │
  ├── L1: bloomFilter.put(orderNo) × N               ← 订单号写入布隆过滤器
  │     日志: [布隆过滤器] 初始化完成，已加载 5 条订单号
  │
  └── L2: redis.set("order:ORD-xxx", orderJson, 30min) × N  ← 完整订单写入 Redis
        日志: [Redis] 预加载完成，成功写入 5/5 条
```

**为什么启动时要预加载？**
- 启动后第一次查询就能命中 Redis，无需等"查询→回填"的过程
- 避免冷启动时的数据库压力峰值

### 二、查询流程（三级穿透）

#### 场景 A：查询不存在的订单号

```
GET /api/order/ORD-99999
  │
  ▼
① bloomFilter.mightContain("ORD-99999") → false（一定不存在）
  │
  └── 直接返回 null ✗ 不查 Redis，不查 DB，0 成本
     日志: [布隆过滤器] 拦截：ORD-99999 一定不存在
```

**Redis 无变化** —— 因为请求在第一层就被拦截了。

#### 场景 B：查询存在的订单号（Redis 已有数据）

```
GET /api/order/ORD-20240601-001
  │
  ▼
① bloomFilter.mightContain(...) → true（可能存在）
  │
  ▼
② redis.get("order:ORD-20240601-001") → 命中 ✓
  │
  └── 反序列化 JSON → ProductOrder 对象 → 返回
     日志: [Redis] 命中缓存：ORD-20240601-001
```

**只读 Redis**，0 次 DB 查询。

#### 场景 C：查询存在的订单号（Redis 缓存过期）

```
GET /api/order/ORD-20240601-001
  │
  ▼
① bloomFilter.mightContain(...) → true（可能存在）
  │
  ▼
② redis.get("order:ORD-20240601-001") → null（未命中/TTL 过期）
  │
  ▼
③ mysql: SELECT * FROM product_order WHERE order_no = 'ORD-20240601-001'
  │  找到数据 ↓
  │
  ├── redis.set("order:ORD-20240601-001", json, 30min)   ← 回填 Redis
  │     日志: [Redis] 回填缓存成功：ORD-20240601-001
  │
  └── 返回 ProductOrder 对象
     日志: [MySQL] 缓存未命中，查询数据库：ORD-20240601-001
```

**这就是"回填"** —— 从 DB 查到的数据写回 Redis，下次同一订单号的查询就能命中 L2 了。

#### 场景 D：布隆过滤器误判（False Positive）

```
GET /api/order/ORD-SIMILAR-BUT-NOT-EXIST
  │
  ▼
① bloomFilter.mightContain(...) → true（误判！实际不存在）
  │
  ▼
② redis.get("order:ORD-SIMILAR-BUT-NOT-EXIST") → null
  │
  ▼
③ mysql: SELECT ... WHERE order_no = 'ORD-SIMILAR-BUT-NOT-EXIST' → 未找到
  │
  ├── redis.set("order:ORD-SIMILAR-BUT-NOT-EXIST", "NULL", 5min)  ← 空值防穿透
  │     日志: [Redis] 写入空值防穿透：ORD-SIMILAR-BUT-NOT-EXIST
  │
  └── 返回 null
```

**空值缓存的作用**：防止同一个不存在的订单号反复穿透到 DB。误判率只有 1%，所以这种情况极少发生。

### 三、创建订单（同步三层）

```
POST /api/order { "orderNo": "ORD-NEW", ... }
  │
  ├── save(order)                          ← 写入 MySQL (L3)
  ├── bloomFilter.add("ORD-NEW")           ← 更新布隆过滤器 (L1)
  └── redis.set("order:ORD-NEW", json, 30min) ← 写入 Redis 缓存 (L2)
```

创建成功后，三个存储层保持一致。

### 四、各层对比总结

| 操作场景 | L1 布隆过滤器 | L2 Redis | L3 MySQL |
|---------|-------------|----------|----------|
| 查询存在订单 | 可能存在 | 命中/回填 | 未命中时查询 |
| 查询不存在订单 | **拦截** | 不查 | 不查 |
| 创建订单 | add | set | insert |
| 启动预加载 | put 订单号 | set 完整对象 | 作为数据源 |

---

## 布隆过滤器原理

### 是什么

布隆过滤器是一种**空间效率极高**的概率性数据结构，用于判断一个元素是否在集合中。

### 工作原理

1. 使用 **k 个哈希函数** 将元素映射到一个 **位数组** 的 k 个位置
2. 插入时，将这 k 个位置都置为 1
3. 查询时，检查这 k 个位置是否都为 1

### 核心特性

| 结果 | 含义 |
|------|------|
| **返回 false** | 元素**一定不存在**于集合中 |
| **返回 true** | 元素**可能存在**于集合中（有误判概率） |

### 为什么不存在误报"false"？

布隆过滤器不会漏报（No False Negative）：只要元素真正存在于集合中，查询一定返回 `true`。
误判只会发生在"不存在但报告存在"的情况（False Positive），概率由 FPP 参数控制。

### 参数配置

[`ProductOrderBloomFilter`](src/main/java/com/example/api/service/ProductOrderBloomFilter.java) 配置：

| 参数 | 值 | 说明 |
|------|-----|------|
| EXPECTED_INSERTIONS | 1,000,000 | 预期插入 100 万条订单号 |
| FPP | 0.01（1%） | 误判率 1% |

Guava 会根据这两个参数自动计算出最优的位数组大小和哈希函数数量。

### Redis 缓存配置

[`ProductOrderService`](src/main/java/com/example/api/service/ProductOrderService.java) 配置：

| 项目 | 值 |
|------|-----|
| Key 格式 | `order:{orderNo}` |
| 序列化方式 | JSON（Jackson） |
| 正常数据 TTL | 30 分钟 |
| 空值防穿透 TTL | 5 分钟 |

---

## 项目结构

```
src/main/java/com/hfh/api/
├── ApiApplication.java              # 启动类（打印地址+连通性测试）
├── config/
│   ├── Knife4jConfig.java           # Knife4j 接口文档配置
│   └── MybatisPlusConfig.java       # MyBatis-Plus 配置
├── entity/
│   └── ProductOrder.java            # 订单实体
├── mapper/
│   └── ProductOrderMapper.java      # MyBatis-Plus Mapper（含全量查询方法）
├── service/
│   ├── ProductOrderBloomFilter.java # L1: 布隆过滤器（启动预加载 L1+L2）
│   └── ProductOrderService.java     # 业务层（三级缓存查询逻辑）
└── controller/
    └── ProductOrderController.java  # REST 接口

src/main/resources/
├── application.yaml                 # 主配置（MySQL + Redis + Knife4j）
└── init.sql                         # 建表和测试数据
```

## API 接口

访问 http://localhost:8080/doc.html 查看 Knife4j 文档。

| 方法 | 路径 | 说明 | 缓存行为 |
|------|------|------|---------|
| GET | `/api/order/{orderNo}` | 查询订单 | L1→L2→L3 三级穿透 |
| POST | `/api/order` | 创建订单 | 同步写入 L1+L2+L3 |

## 快速开始

### 前置条件

- JDK 25+
- MySQL 8.0+
- Redis 6.0+

### 步骤

```bash
# 1. 执行建表 SQL
mysql -u root -p < src/main/resources/init.sql

# 2. 修改 application.yaml 中的连接信息（如密码）

# 3. 启动应用
./gradlew bootRun                    # Windows: gradlew.bat bootRun
```

### 启动日志确认

```
============================================
  Knife4j : http://localhost:8080/doc.html
============================================
[MySQL] success - jdbc:mysql://localhost:3306/spring_bool_filter
[Redis] success
[布隆过滤器] 初始化完成，已加载 5 条订单号
[Redis] 预加载完成，成功写入 5/5 条
Started ApiApplication in X seconds
```

### 测试验证

```bash
# 1. 查询存在的订单（应命中 Redis）
curl http://localhost:8080/api/order/ORD-20240601-001
# 日志: [Redis] 命中缓存：ORD-20240601-001

# 2. 查询不存在的订单（被布隆过滤器拦截）
curl http://localhost:8080/api/order/ORD-99999
# 日志: [布隆过滤器] 拦截：ORD-99999 一定不存在
# 注意: 此时 Redis 不会有任何变化（第一层就拦截了）

# 3. 创建新订单
curl -X POST http://localhost:8080/api/order \
  -H "Content-Type: application/json" \
  -d '{"orderNo":"ORD-TEST","productName":"测试商品","quantity":1,"unitPrice":10.00}'
# 日志: [布隆过滤器] 新增：ORD-TEST
#       [Redis] 写入新订单缓存：ORD-TEST
```
