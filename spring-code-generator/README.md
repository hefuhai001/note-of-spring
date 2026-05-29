# Spring Code Generator

基于 Spring Boot 3 + Velocity 模板引擎的代码生成器，连接 PostgreSQL 数据库读取表结构，一键生成 Entity / DTO / Mapper / Service / Controller 全套代码。

## 技术栈

| 库 | 版本 | 干什么用 |
|---|---|---|
| Spring Boot | 3.5.4 | 基础框架，提供 Web 容器与自动装配 |
| Velocity | 2.3 | 模板引擎，合并 `.vm` 模板与变量输出 Java 源码 |
| MyBatis-Plus | 3.5.7 | ORM 层，用注解 SQL 查 `information_schema` 拿表/列元数据；自带分页插件 |
| PostgreSQL | 42.6.0 | JDBC 驱动，连 PG 库读表结构 |
| Knife4j | 4.5.0 | OpenAPI 3 文档增强，启动后访问 `/doc.html` 即可调试接口 |

## 架构一图流

```
PostgreSQL (information_schema)
        │
        ▼
 DatabaseMapper (@Select 原生SQL查表结构)
        │
        ▼
 DatabaseServiceImpl (组装 TableInfo / ColumnInfo)
        │
        ▼
 CodeGenerationController (REST API)
        │
        ▼
 VelocityTemplateService (VelocityEngine.merge)
        │
        ▼
 resources/templates/*.vm ──→ 生成的 Java 源码字符串
```

## 内置模板一览

| 模板文件 | 生成目标 | 必填变量 |
|---|---|---|
| `java-class.vm` | Entity 实体类 | `packageName` `className` `fields` |
| `request-dto.vm` | 请求 DTO | `packageName` `className` `fields`（可选 `classComment`） |
| `mapper.vm` | Mapper 接口 | `packageName` `className` `entityName` `primaryKeyType` |
| `mapper-xml.vm` | Mapper XML | `namespace` `entityName` `tableName` `fields` `primaryKey` |
| `service.vm` | Service 接口 | `packageName` `className` `entityName` `primaryKeyType` |
| `service-impl.vm` | Service 实现 | `packageName` `className` `entityName` `primaryKeyType` `mapperName` |
| `controller.vm` | REST Controller | `packageName` `className` `entityName` `entityNameLower` `idType` |

## API 接口

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/api/code/generate` | 传入模板名 + 变量，返回生成的代码字符串 |
| GET | `/api/code/templates` | 列出所有可用模板及其参数定义 |
| GET | `/api/code/template/{name}` | 获取单个模板的参数定义 |
| GET | `/api/code/example/{name}` | 获取模板示例数据 |
| GET | `/api/code/database/tables` | 查询 PG 库中 public schema 下所有表 |
| GET | `/api/code/database/table/{schema}/{table}` | 查询指定表的元信息 + 列详情 |

## 快速开始

**1. 建库 & 配置连接**

`application.yml` 中配置 PostgreSQL 数据源：

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/your_db
    username: your_user
    password: your_pass
```

**2. 启动**

```bash
mvn spring-boot:run
```

启动后控制台打印：

```
Application: 'spring-code-generator' is running Success!
Local URL:    http://localhost:8080
Document:     http://localhost:8080/doc.html
```

**3. 调用生成接口**

```bash
curl -X POST http://localhost:8080/api/code/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templateName": "service.vm",
    "variables": {
      "packageName": "com.example.service",
      "className": "UserService",
      "entityName": "User",
      "primaryKeyType": "Long"
    }
  }'
```

返回值即生成的 Java 源码，直接复制到项目即可。

**4. 从数据库表生成**

先查表结构：

```bash
curl http://localhost:8080/api/code/database/tables
curl http://localhost:8080/api/code/database/table/public/t_user
```

拿到列信息后拼装 variables 调生成接口。

## 第三方库要点速查

### Velocity 2.3

- 语法：`$变量`、`#foreach`、`#if`、`#set`
- 本项目用 `ClasspathResourceLoader` 从 `resources/templates/` 加载 `.vm` 文件
- 核心 API：`VelocityEngine.getTemplate()` → `VelocityContext.put()` → `template.merge(context, writer)`

### MyBatis-Plus 3.5.7

- 本项目仅用其注解 SQL 能力（`@Select`）+ 分页插件，未用代码生成器
- `MybatisPlusInterceptor` + `PaginationInnerInterceptor(DbType.POSTGRE_SQL)` 开启 PG 分页
- 查询全走 `information_schema` 系统视图：`tables`、`columns`、`table_constraints`、`key_column_usage`、`pg_description`

### Knife4j 4.5.0

- 基于 SpringDoc OpenAPI 3，兼容 Jakarta 命名空间
- 自动扫描 `@RestController` 生成文档
- 访问 `/doc.html` 可视化调试，无需额外配置

### PostgreSQL JDBC 42.6.0

- 驱动类 `org.postgresql.Driver`，Spring Boot 自动识别
- 支持 PG 特有类型（JSONB、UUID 等）和 `pg_description` 系统表注释查询
