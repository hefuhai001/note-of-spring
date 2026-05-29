# Spring PostgreSQL

Spring Boot 3 + MyBatis-Plus + PostgreSQL + Knife4j 后端脚手架。

## 技术栈

| 组件 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.1.4 | 基础框架 |
| MyBatis-Plus | 3.5.11 | ORM |
| Knife4j | 4.5.0 | 接口文档 (OpenAPI3) |
| PostgreSQL | - | 数据库 |
| Caffeine | - | 本地缓存 |
| HikariCP | - | 连接池 |

## 第三方库详解

### MyBatis-Plus 3.5.11

MyBatis 的增强工具，**只做增强不做改变**。

**核心能力：**

- **CRUD 零代码**：继承 `BaseMapper<T>` 即获得单表增删改查
- **条件构造器**：`QueryWrapper` / `UpdateWrapper` 链式调用，告别手写 SQL 拼接
- **分页插件**：`PaginationInnerInterceptor` 一行配置生效
- **代码生成**：`AutoGenerator` 根据表结构逆向生成 Entity/Mapper/Service/Controller
- **逻辑删除 / 自动填充 / 乐观锁**：注解驱动，开箱即用

**本项目用法：**

```java
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // 已内置: selectById / insert / updateById / deleteById
    // 已内置: selectList / selectPage / selectCount ...
    // 只需写复杂 SQL
}
```

> 💡 **选型理由**：相比 JPA，SQL 可控性强；相比原生 MyBatis，单表操作省掉 80% XML。国内生态最成熟的 MyBatis 增强方案。

---

### Knife4j 4.5.0

基于 OpenAPI3 规范的增强版 Swagger，**国产，界面比原版好看 10 倍**。

**核心能力：**

- **接口文档自动生成**：基于注解 `@Tag` / `@Operation` / `@Parameter` / `@Schema`
- **在线调试**：前端直接在页面发请求，免 Postman
- **JSON 高亮**：响应结果格式化展示
- **导出文档**：支持 Markdown / HTML / PDF 离线文档
- **Basic 认证**：生产环境可加密码保护

**访问地址：** `http://localhost:9999/doc.html`

**本项目配置要点：**

```yaml
knife4j:
  enable: true
  production: false          # 生产环境设为 true 关闭文档
  basic:
    enable: true             # 开启 BasicAuth 保护
    username: 1
    password: ${DB_PASSWORD}
```

> 💡 **选型理由**：Swagger 原版 UI 丑且多年不维护，Knife4j 是目前 Java 领域最佳 API 文档方案。

---

### Caffeine (Spring Cache)

Google 出品的高性能本地缓存库，**比 Guava Cache 快一个数量级**。

**核心能力：**

- **LRU 淘汰策略**：基于 Window TinyLFU，命中率高
- **异步刷新**：`refreshAfterWrite` 过期后异步加载，不阻塞请求
- **统计监控**：`hitRate()` / `evictionCount()` / `averageLoadPenalty()`
- **与 Spring Cache 无缝集成**：`@Cacheable` / `@CacheEvict` 直接用

**本项目配置：**

```yaml
spring:
  cache:
    type: caffeine
    caffeine:
      spec: maximumSize=10000,expireAfterWrite=600s
```

含义：最大缓存 10000 条，写入 10 分钟后过期。

> 💡 **适用场景**：读多写少、数据量可控、允许短延迟的场景（如字典表、配置项）。大数据量或强一致性需求请上 Redis。

---

### HikariCP

Spring Boot 2.x 默认连接池，**目前业界最快 JDBC 连接池**。

**本项目调优参数：**

| 参数 | 值 | 说明 |
|------|-----|------|
| maximum-pool-size | 50 | 最大连接数 |
| minimum-idle | 10 | 最小空闲连接 |
| connection-timeout | 30s | 获取连接超时 |
| idle-timeout | 10min | 空闲连接回收时间 |
| max-lifetime | 30min | 连接最大存活时间 |
| leak-detection-threshold | 60s | 连接泄漏检测（开发必开） |

> ⚠️ **生产建议**：`maximum-pool-size` 按 `核心数 * 2 + 有效连接数` 估算，别盲目拉大。开启 `leak-detection-threshold` 能快速定位连接未释放的 bug。

---

## 项目结构

```
src/main/java/com/example/demo/
├── SpringApplication.java       # 启动类
├── controller/
│   └── UserController.java      # REST 接口层
├── entity/
│   └── User.java                # 实体类
├── mapper/
│   └── UserMapper.java          # DAO 层 (MyBatis-Plus)
└── service/
    └── UserService.java         # 业务逻辑层

src/main/resources/
├── application.yml              # 主配置
├── application-dev.yml          # 开发环境
├── application-prod.yml         # 生产环境
└── mapper/
    └── UserMapper.xml           # SQL 映射文件
```

## 启动方式

```bash
# 默认启动 dev 环境
mvn spring-boot:run

# 指定环境
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

启动成功后：
- 应用地址：`http://localhost:9999`
- 接口文档：`http://localhost:9999/doc.html`
- 接口示例：`http://localhost:9999/users`

## 多环境配置

| 环境 | Profile | 用途 |
|------|---------|------|
| 开发 | dev | 本地调试 |
| 测试 | test | 测试服务器 |
| 预发布 | pre | 预上线验证 |
| 生产 | prod | 正式环境 |

切换方式：修改 `application.yml` 中 `spring.profiles.active` 或启动时指定 `-Dspring.profiles.active=xxx`。
