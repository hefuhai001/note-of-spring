# Spring Data JPA

Spring Boot 3.5 + Spring Data JPA + H2 + Knife4j 的 CRUD 示例项目。

## 依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| spring-boot-starter-data-jpa | 3.5.0 | JPA ORM，自动生成 SQL |
| spring-boot-starter-web | 3.5.0 | REST API |
| h2 | runtime | 内存数据库，零配置 |
| knife4j-openapi3-jakarta-spring-boot-starter | 4.5.0 | Swagger UI 增强，API 文档 |

## 项目结构

```
src/main/java/com/example/demo/
├── base/          # 统一响应封装 (Response, ResponseCode)
├── controller/    # REST 接口
├── entity/        # JPA 实体
├── initializer/   # 数据初始化 & 配置
├── repository/    # 数据访问层
└── service/       # 业务逻辑层
```

## 核心用法

### 1. 实体映射

```java
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
}
```

- `@Entity` → 声明 JPA 实体
- `@Table(name = "t_user")` → 指定表名，不加则默认类名小写
- `@Id` + `@GeneratedValue(strategy = GenerationType.IDENTITY)` → 主键自增

### 2. Repository — 零 SQL 实现 CRUD

```java
public interface UserRepository extends JpaRepository<User, Long> {
}
```

继承 `JpaRepository` 即可获得：

| 方法 | 作用 |
|------|------|
| `findAll()` | 查全部 |
| `findById(id)` | 按 ID 查，返回 `Optional<T>` |
| `save(entity)` | 新增/更新（有 ID 更新，无 ID 新增） |
| `deleteById(id)` | 按 ID 删 |
| `count()` | 计数 |
| `findAll(Sort)` | 排序查询 |
| `findAll(Pageable)` | 分页查询 |

**派生查询**：按方法名自动生成 SQL，无需写实现：

```java
List<User> findByName(String name);           // WHERE name = ?
List<User> findByEmailContaining(String kw);  // WHERE email LIKE '%kw%'
User findByNameAndEmail(String n, String e);  // WHERE name = ? AND email = ?
```

关键字：`And`、`Or`、`Between`、`LessThan`、`GreaterThan`、`Like`、`Containing`、`OrderBy`、`In`、`Not`、`IsNull`、`IsNotNull`

### 3. 配置要点

```properties
# H2 内存库，重启清空
spring.datasource.url=jdbc:h2:mem:testdb
# 文件持久化（取消注释即可）
#spring.datasource.url=jdbc:h2:file:./data/testdb

# ddl-auto 取值：
#   update   → 表结构自动同步（开发用）
#   create   → 每次启动重建表
#   validate → 只校验不修改（生产用）
#   none     → 什么都不做
spring.jpa.hibernate.ddl-auto=update

# H2 控制台：http://localhost:8000/h2-console
spring.h2.console.enabled=true
```

### 4. 分页查询

```java
Page<User> page = userRepository.findAll(
    PageRequest.of(0, 10, Sort.by("id").descending())
);
page.getContent();       // 当前页数据
page.getTotalElements(); // 总条数
page.getTotalPages();    // 总页数
```

## API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/users` | 查所有用户 |
| GET | `/users/{id}` | 按 ID 查用户 |
| POST | `/users` | 新增用户 |
| DELETE | `/users/{id}` | 删除用户 |

API 文档：启动后访问 `http://localhost:8000/doc.html`（Knife4j）

## 启动

```bash
mvn spring-boot:run
```

H2 控制台：`http://localhost:8000/h2-console`，JDBC URL 填 `jdbc:h2:mem:testdb`，用户名 `sa`，密码 `sa`。