# Spring Boot 集成 H2 数据库

## 技术栈

| 组件 | 版本 |
|------|------|
| Spring Boot | 3.4.4 |
| Java | 17 |
| H2 Database | (由 spring-boot-starter-parent 管理) |
| Spring Data JPA | (同上) |

## 依赖（pom.xml 核心部分）

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

**注意**：`h2` 的 scope 设为 `runtime` 即可，编译阶段不需要。

## H2 是什么

纯 Java 写的嵌入式关系数据库，支持标准 SQL。特点：

- **零配置启动**：无需安装，加依赖就能跑
- **三种运行模式**：
  - `jdbc:h2:mem:testdb` — 内存模式，进程结束数据清空（开发测试首选）
  - `jdbc:h2:file:./data/testdb` — 文件模式，数据持久化到磁盘
  - `jdbc:h2:tcp://localhost/~/testdb` — 服务端模式，支持远程连接
- **自带 Web 控制台**：浏览器直接操作数据库，调试利器

## 配置详解

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=${DB_PASSWORD}

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

逐行说明：

| 配置项 | 作用 |
|--------|------|
| `datasource.url` | 连接地址，三种模式见上文 |
| `datasource.username/password` | 默认账号密码都是 `sa` |
| `h2.console.enabled` | 开启 Web 管理界面 |
| `h2.console.path` | 控制台访问路径 |
| `jpa.ddl-auto=update` | Hibernate 自动根据 Entity 建表/更新表结构 |
| `jpa.show-sql=true` | 控制台打印 SQL，方便调试 |

**生产环境务必关掉 `h2.console` 和 `show-sql`**。

## 项目代码结构

```
src/main/java/com/example/h2/
├── SpringH2Application.java   # 启动类
├── Person.java                # JPA 实体
├── PersonRepository.java      # Data JPA Repository 接口
├── PersonService.java         # 业务逻辑层
└── PersonController.java      # REST API 层
```

### Entity

```java
@Entity
public class Person {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;
}
```

### Repository

```java
public interface PersonRepository extends JpaRepository<Person, Long> {}
```

继承 `JpaRepository` 后自动获得 `save()`、`findAll()`、`findById()`、`delete()` 等方法，零实现代码。

### Controller API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/persons/add` | 创建 Person（传 JSON body） |
| GET | `/persons/list` | 查询所有 Person |

> ⚠️ 注意：当前实现用了 `@GetMapping` 做"新增"操作，实际项目中应改为 `@PostMapping`。这里保留原样仅作演示。

## 启动后验证

1. 运行 `SpringH2Application.main()`
2. 浏览器打开 `http://localhost:8080/h2-console`
3. JDBC URL 填 `jdbc:h2:mem:testdb`，账号密码 `sa/sa`，点 Connect
4. 可看到 Hibernate 自动创建的 `PERSON` 表

## H2 vs 其他嵌入式数据库对比

| 特性 | H2 | SQLite | Derby |
|------|-----|--------|-------|
| 纯 Java 实现 | ✅ | ❌(C语言) | ✅ |
| 内存模式 | ✅ | ❌ | ✅ |
| Web 控制台 | ✅ | ❌ | ❌ |
| 兼容 MySQL/PostgreSQL 语法 | ✅(兼容模式) | 部分 | 部分 |
| 性能 | 快 | 最快 | 中等 |

## 常用场景

- **单元测试 / 集成测试**：内存模式，每次跑完自动清理
- **原型开发 / POC**：快速验证业务逻辑，不用搭数据库
- **本地调试**：Web 控制台直接看数据

**不适合**：生产环境存储业务数据（除非有特殊需求且充分评估）。
