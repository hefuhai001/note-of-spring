# Spring Generator

基于 Spring Boot 3.5 + Velocity 的代码生成器，连接数据库读取表结构，一键生成 Java / TypeScript / Vue 全栈代码。

## 技术栈

| 类别 | 技术 | 版本 | 用途 |
|------|------|------|------|
| 基础框架 | Spring Boot | 3.5.6 | Web 容器 + 自动配置 |
| ORM | MyBatis-Plus | 3.5.12 | 数据库表结构读取，内置 `jsqlparser` 支持 SQL 解析 |
| API 文档 | Knife4j | 4.5.0 | OpenAPI 3 规范，Swagger UI 增强版（中文界面 + BasicAuth） |
| 模板引擎 | Apache Velocity | 2.3 | 核心代码生成引擎，`.vm` 模板渲染 |
| 模板引擎 | FreeMarker | - | 备用模板方案 |
| 数据库 | PostgreSQL | - | 元数据源（可替换为 MySQL 等） |
| 缓存 | Caffeine | - | 本地缓存（10K 容量，10min 过期） |
| 工具库 | Lombok | - | 注解驱动，消除样板代码 |

## 核心依赖说明

### MyBatis-Plus 3.5.12

本项目不用于业务 CRUD，而是利用其 **数据库元数据读取能力**：

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    <version>3.5.12</version>
</dependency>
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-jsqlparser</artifactId>
    <version>3.5.12</version>
</dependency>
```

- `mybatis-plus-spring-boot3-starter`：适配 Spring Boot 3.x（Jakarta命名空间）
- `mybatis-plus-jsqlparser`：SQL 解析扩展，支持复杂 SQL 分析
- 通过 `DatabaseMapper` 直连 PG 的 `information_schema` 读取列信息（字段名、类型、注释、是否可空）

### Knife4j 4.5.0

国内Swagger增强方案，基于 OpenAPI 3 规范：

```yaml
knife4j:
  enable: true
  setting:
    language: zh_cn
  basic:
    enable: true
    username: 1
    password: ${DB_PASSWORD}
```

- 访问地址：`http://localhost:9900/doc.html`
- 内置 BasicAuth 保护（账号密码均为 `1`）
- 中文界面，支持接口调试、离线文档导出

### Apache Velocity 2.3

核心模板引擎，11 个 `.vm` 模板覆盖全栈代码生成：

```
resources/vm/
├── java/
│   ├── entity.vm          # JPA/MyBatis-Plus 实体类
│   ├── controller.vm      # REST Controller
│   ├── service.vm         # Service 接口
│   ├── service-impl.vm    # Service 实现
│   ├── mapper.vm          # Mapper 接口
│   ├── mapper-xml.vm      # MyBatis XML
│   ├── bo.vm              # 业务对象
│   └── vo.vm              # 视图对象
├── ts/
│   ├── api.vm             # TypeScript API 调用
│   └── types.vm           # TypeScript 类型定义
└── vue/
    └── index.vue          # Vue 页面骨架
```

### Caffeine 本地缓存

Spring Boot 内置缓存抽象的高性能实现：

```yaml
spring:
  cache:
    type: caffeine
    caffeine:
      spec: maximumSize=10000,expireAfterWrite=600s
```

- 用于缓存数据库元数据，避免重复查询 `information_schema`
- 比 Redis 更适合本地高频小数据场景（零网络开销）

## 接口列表

| 方法 | 路径 | 功能 |
|------|------|------|
| POST | `/api/code/generate` | 单模板生成 |
| POST | `/api/code/generate/all` | 批量生成所有模板（返回 JSON） |
| POST | `/api/code/generate/all/zip` | 批量生成并打包下载 ZIP |
| GET | `/api/database/tables` | 获取数据库表列表 |
| GET | `/api/database/tables/{tableName}/columns` | 获取表字段详情 |
| GET | `/api/template/list` | 获取可用模板列表 |

## 启动方式

```bash
mvn spring-boot:run
```

服务端口：`9900`
API 文档：`http://localhost:9900/doc.html`

## 配置要点

- 数据源配置在 `application-dev.yml`
- 默认激活 `dev` profile
- MyBatis-Plus 开启了 SQL 日志（`StdOutImpl`），生产环境建议关闭
- 文件上传限制 10MB（ZIP 下载场景）

## 扩展指南

**新增模板**：在 `resources/vm/` 下新建 `.vm` 文件，并在 `CodeGenerationController.templateToFileMap` 中注册映射关系。

**新增数据源支持**：修改 `DatabaseServiceImpl`，更换 JDBC 驱动和 `information_schema` 查询方言。
