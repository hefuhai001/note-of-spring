# Spring Docker Code

基于 Spring Boot + Docker 的多语言在线代码执行引擎。提交源代码，容器内编译/运行，返回结果。

## 支持语言

| 语言 | 标识 | 编译 | 运行 |
|------|------|------|------|
| C | `c` | gcc | ./exe |
| C++ | `cpp` | g++ | ./exe |
| Java | `java` | javac | java Main |
| Python | `python` | - | python3 |
| JavaScript | `javascript` | - | node |
| Rust | `rust` | rustc | ./exe |
| Go | `go` | go build | ./exe |
| C# | `dotnet` | - | dotnet-script |

## 技术栈

- **Java 21** / **Spring Boot 3.5.6**
- **Docker Java 3.3.6** — 远程容器管理，代码隔离执行
- **MyBatis-Plus 3.5.7** — ORM
- **PostgreSQL** — 存储（`text[]` 原生数组存参数）
- **Sa-Token 1.40.0** — 鉴权
- **Knife4j 4.5.0** — API 文档
- **commons-compress** — tar 归档拷贝文件到容器

## 项目结构

```
src/main/java/asia/hfh/code/
├── core/                          # 核心库（可复用，不依赖业务实体）
│   ├── DockerExecUtils.java       # 底层Docker操作：tar打包/拷贝/执行/输出收集
│   ├── LanguageConfig.java        # 语言配置枚举：扩展名/编译模板/运行模板/错误关键词
│   └── DockerCodeRunner.java      # 高层编排：拷贝→编译→运行→收集结果
├── controller/
│   ├── CodeRunController.java     # POST /api/task/create  GET /api/task/result/{exeName}
│   └── TaskController.java        # 任务CRUD
├── service/
│   ├── CodeRunService.java        # 代码执行接口
│   ├── TaskService.java           # 任务CRUD接口
│   └── impl/
│       ├── CodeRunServiceImpl.java
│       └── TaskServiceImpl.java
├── entity/
│   └── TaskEntity.java            # 任务表实体
├── bo/
│   └── CreateTaskRequest.java     # 创建任务DTO
├── handler/
│   └── StringListTypeHandler.java # PostgreSQL text[] ↔ List<String>
├── mapper/
│   └── TaskMapper.java
├── config/
│   ├── DockerConfig.java          # Docker客户端连接配置
│   └── MybatisPlusConfig.java     # 分页/自动填充
├── exception/
│   └── ExceptionHandle.java       # 全局异常处理
└── base/
    └── BaseEntity.java            # 公共字段
```

## 快速开始

### 1. 建表

```sql
-- 见 sql/public.sql
CREATE TABLE "public"."gcc_task" (
    "id"         BIGSERIAL NOT NULL PRIMARY KEY,
    "exe_name"   varchar(100) NOT NULL,
    "language"   varchar(32)  NOT NULL DEFAULT 'c',
    "code"       text         NOT NULL,
    "args"       text[],
    "status"     varchar(20)  NOT NULL DEFAULT 'CREATED',
    "result"     text,
    "created_at" timestamptz(6) DEFAULT now(),
    "updated_at" timestamptz(6) DEFAULT now()
);
```

### 2. 构建Docker容器

```bash
cd docker
docker compose up -d --build
```

容器内含 gcc/g++/openjdk17/python3/nodejs/rust/go/dotnet7，以 `nobody` 用户运行代码，3秒超时。

### 3. 配置 application-dev.yml

```yaml
docker:
  host: tcp://127.0.0.1:2375
  tls-verify: false
  container-name: code-runner
```

### 4. 启动

```bash
mvn spring-boot:run
```

## API

### 创建任务

```
POST /api/task/create
Content-Type: application/json

{
  "language": "python",
  "code": "print('hello')",
  "args": []
}
```

返回：

```json
{
  "success": true,
  "exeName": "e9e40fcb-ce75-4ee9-be9a-159411ebe3b0",
  "language": "python",
  "message": "任务创建成功"
}
```

### 查询结果

```
GET /api/task/result/{exeName}
```

返回：

```json
{
  "success": true,
  "exeName": "e9e40fcb-...",
  "language": "python",
  "status": "COMPLETED",
  "result": "hello",
  "createTime": "2026-05-20 23:01:50",
  "updateTime": "2026-05-20 23:01:50"
}
```

`status` 取值：`CREATED` → `RUNNING` → `COMPLETED` / `ERROR`

## 新增语言

在 `LanguageConfig` 枚举中加一行即可，零代码改动：

```java
KOTLIN(
    "kotlin", ".kt", false,
    "kotlinc {srcFile} -include-runtime -d {exeFile}.jar",
    "su nobody -s /bin/sh -c 'java -jar {exeFile}.jar {args}'",
    List.of("error:", "ERROR")
),
```

如果语言要求类名与文件名一致（如Java），设置 `useSubDir = true`，系统会自动创建独立子目录。
