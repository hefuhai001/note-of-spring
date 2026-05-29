# Spring Docker GCC - 在线 C 语言编译运行平台

基于 Spring Boot + Docker + Vue3 的在线 C 代码编译运行环境，支持编译/运行分离、沙箱执行。

## 架构

```
┌─────────────┐     ┌──────────────┐     ┌──────────────┐
│   Vue3 前端   │────▶│ Spring Boot  │────▶│  GCC 容器     │
│  (CodeMirror) │     │    后端        │     │ (Alpine+GCC) │
└─────────────┘     └──────────────┘     └──────────────┘
       :5173              :8080             docker exec
```

## 技术栈

| 层 | 技术 | 版本 |
|---|------|------|
| 后端 | Java / Spring Boot | 21 / 4.0.0 |
| 容器编排 | Docker Compose | 3.8 |
| 编译器镜像 | Alpine + GCC | 12 |
| 前端 | Vue 3 + Vite | 3.5 / 7.3 |
| 编辑器 | CodeMirror 6 | 6.x |
| HTTP 客户端 | Axios | 1.x |

## 核心依赖说明

### 后端
- **docker-java-core** `3.3.4`：Docker Remote API 的 Java 客户端，用于容器内执行命令
- **docker-java-transport-httpclient5** `3.3.6`：传输层实现，基于 Apache HttpClient5
- **httpclient5** `5.3.1`：必须显式引入，否则运行时缺包报错

### 前端
- **codemirror** `6.x`：编辑器核心，轻量无 DOM 污染
- **@codemirror/lang-cpp**：C/C++ 语法高亮
- **@codemirror/theme-one-dark**：暗色主题
- **axios**：HTTP 请求库，二次封装了拦截器和错误处理
- **qs**：URL 参数序列化，处理数组参数时用 `brackets` 格式

## API 接口

| 方法 | 路径 | 功能 | 返回值 |
|------|------|------|--------|
| POST | `/api/compile` | 编译代码 | taskId（UUID）或错误信息 |
| POST | `/api/run` | 运行已编译程序 | 程序输出 |
| POST | `/api/compile_run` | 编译并一步运行 | 编译结果 + 运行输出 |

### 请求示例

**编译**
```json
POST /api/compile
{ "code": "#include <stdio.h>\nint main(){ return 0; }" }
// → "550e8400-e29b-41d4-a716-446655440000"
```

**运行**
```json
POST /api/run
{ "taskId": "uuid", "args": ["arg1", "arg2"] }
// → "Hello World\n"
```

**编译并运行**
```json
POST /api/compile_run
{ "code": "...", "args": ["10"] }
// → "编译通过(uuid) \n 输出内容"
```

## 快速启动

### 1. 启动 GCC 容器
```bash
docker-compose up -d --build
```

这会创建一个名为 `gcc-tiny` 的容器，内含 GCC 12，工作目录为 `/workspace`。

### 2. 启动后端
```bash
cd spring-docker-gcc
mvn spring-boot:run
```

配置文件 `application.yml` 中设置：
```yaml
docker:
  container-name: gcc-tiny
```

### 3. 启动前端
```bash
cd v-code
npm install
npm run dev
```

访问 http://localhost:5173

## 项目结构

```
spring-docker-gcc/
├── src/main/java/.../
│   ├── controller/GccController.java      # REST 接口层
│   ├── service/                           # 业务逻辑（编译/运行分离）
│   └── dto/                               # 请求参数对象
├── v-code/src/
│   ├── components/CodeEditor.vue          # CodeMirror 编辑器组件
│   ├── components/HelloWorld.vue          # 主页面（工具栏+按钮）
│   ├── api/compile.js                     # API 封装
│   └── utils/request.js                   # Axios 二次封装
├── docker-compose.yml                     # GCC 容器编排
└── Dockerfile                             # Alpine + GCC 镜像
```

## 安全机制

- 使用 `su nobody -s /bin/sh` 以低权限用户执行程序
- `timeout 3s` 限制运行时间防死循环
- 每次编译生成唯一 UUID 文件名，避免冲突

## 关键设计决策

**为什么编译和运行拆成两个接口？**

1. 复用性：一次编译可多次运行不同参数
2. 调试效率：编译失败时无需重复上传代码
3. 任务追踪：taskId 可关联日志、缓存等后续扩展

**为什么用 Docker 执行而非直接调用系统 GCC？**

隔离性。用户代码在独立容器中运行，不影响宿主机安全。
