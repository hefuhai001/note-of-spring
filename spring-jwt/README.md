# Spring Boot 3 + JWT 认证实战

基于 **Spring Boot 3.1.5 + Java 17** 的 JWT 认证完整示例，包含拦截器、注解、统一响应等核心机制。

## 技术栈

| 层级 | 技术 | 版本 |
|------|------|------|
| 后端框架 | Spring Boot | 3.1.5 |
| ORM | MyBatis-Plus | 3.5.3.1 |
| JWT | java-jwt (Auth0) | 3.4.1 |
| API 文档 | Knife4j (Swagger3) | 4.1.0 |
| Excel 处理 | EasyExcel | 3.2.1 |
| 参数校验 | Hibernate Validator | 8.0.0.Final |
| 前端 | Vue 3 + TypeScript + Vite | - |
| 数据库 | MySQL | - |

---

## 核心依赖详解

### 1️⃣ java-jwt (Auth0) — JWT 核心库

```xml
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>3.4.1</version>
</dependency>
```

**用途**：Token 生成、签名、解析、过期验证。

**关键用法**（见 [JWTUtils.java](jwt-api/src/main/java/com/example/hfh/utils/JWTUtils.java)）：

```java
// ✅ 生成 Token
Map<String, String> payload = new HashMap<>();
payload.put("userId", "1001");
payload.put("role", "admin");
String token = JWT.create()
    .withClaim("userId", "1001")      // Payload 载荷
    .withExpiresAt(new Date(System.currentTimeMillis() + 3600000)) // 1小时过期
    .sign(Algorithm.HMAC256("your-secret")); // HMAC256 签名

// ✅ 解析 & 验证 Token
JWTVerifier verifier = JWT.require(Algorithm.HMAC256("your-secret")).build();
DecodedJWT decoded = verifier.verify(token); // 自动校验签名和过期时间
String userId = decoded.getClaim("userId").asString();
```

**注意**：
- `SECRET` 密钥必须与服务端一致
- `EXPIRE_TIME` 控制过期时长（单位：秒）
- `resolveToken()` 内部会抛异常如果 Token 无效或过期

---

### 2️⃣ MyBatis-Plus — ORM 增强

```xml
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-boot-starter</artifactId>
    <version>3.5.3.1</version>
</dependency>
```

**用途**：无需手写 SQL 即可完成 CRUD，支持代码生成器。

**配置**（[application.yml](jwt-api/src/main/resources/application.yml)）：
```yaml
mybatis-plus:
  mapper-locations: classpath*:mapper/*.xml
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl # 打印SQL日志
```

**核心能力**：
- 内置 `BaseMapper<T>` 泛型 CRUD
- 分页插件配置见 [MybatisPlusConfig.java](jwt-api/src/main/java/com/example/hfh/config/MybatisPlusConfig.java)
- 代码生成器（配合 Freemarker 模板）自动生成 Entity/Mapper/Service/Controller

---

### 3️⃣ Knife4j (Swagger3) — 在线 API 文档

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
    <version>4.1.0</version>
</dependency>
```

**访问地址**：`http://localhost:9090/doc.html`

**配置**（[SwaggerConfig.java](jwt-api/src/main/java/com/example/hfh/config/SwaggerConfig.java)）：
- 自动扫描所有 `@RestController` 接口
- 支持中文界面（`language: zh_cn`）
- 可在 Controller 方法上使用 `@Parameter` / `@Operation` 补充文档说明

---

### 4️⃣ Lombok — 消除样板代码

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```

**常用注解**：
- `@Data` — 自动生成 getter/setter/equals/hashCode/toString
- `@Builder` — 建造者模式
- `@NoArgsConstructor` / `@AllArgsConstructor` — 构造函数
- `@Slf4j` — 日志对象

---

### 5️⃣ EasyExcel — Excel 导入导出

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>easyexcel</artifactId>
    <version>3.2.1</version>
</dependency>
```

**特点**：阿里开源，内存占用低（相比 Apache POI），适合大数据量 Excel 操作。

---

### 6️⃣ Hibernate Validator — 参数校验

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

**常用注解**：
- `@NotNull` / `@NotBlank` / `@NotEmpty`
- `@Email` / `@Pattern(regexp="")`
- `@Size(min=, max=)` / `@Min` / `@Max`
- 在 Controller 参数前加 `@Valid` 触发校验

---

## 项目架构

```
spring-jwt/
├── jwt-api/                          # 后端服务 (端口: 9090)
│   └── src/main/java/com/example/jwt/
│       ├── annotation/
│       │   └── JwtToken.java         # 自定义注解：标记需要认证的接口
│       ├── config/
│       │   ├── JwtInterceptor.java   # JWT 拦截器：校验 Token
│       │   ├── WebConfig.java        # 注册拦截器
│       │   ├── SwaggerConfig.java    # Swagger 配置
│       │   ├── GlobalControllerAdvice.java  # 全局异常处理
│       │   └── MybatisPlusConfig.java      # MP 分页插件
│       ├── controller/
│       │   └── BaseController.java  # 示例接口：登录 + 测试
│       ├── resp/
│       │   ├── ApiResponse.java      # 统一响应封装
│       │   └── ApiResponseCode.java  # 响应码枚举
│       └── utils/
│           └── JWTUtils.java         # JWT 工具类
├── jwt-ui/                           # 前端 (Vue 3 + TS + Vite)
│   └── src/
│       ├── api/index.ts              # API 请求封装
│       ├── request/index.ts          # Axios 拦截器
│       └── store/index.ts            # Pinia 状态管理
└── README.md
```

---

## JWT 认证流程

```
客户端                    服务端
  │                         │
  ├─ POST /common/login ──→ │  验证账号密码
  │  {acc, pwd}             │  ↓ 通过
  │←─ 返回 Token ──────────│  JWTUtils.generateToken()
  │                         │
  ├─ POST /common/test ───→│  Header: token=xxx
  │  Header: token          │  ↓ JwtInterceptor 拦截
  │                         │  1. 检查 @JwtToken 注解
  │←─ 返回数据 ─────────────│  2. resolveToken() 验证
  │                         │  3. 放行 / 抛出异常
```

**关键组件**：

| 组件 | 文件 | 作用 |
|------|------|------|
| `@JwtToken` 注解 | [JwtToken.java](jwt-api/src/main/java/com/example/hfh/annotation/JwtToken.java) | 标记需要认证的接口方法 |
| 拦截器 | [JwtInterceptor.java](jwt-api/src/main/java/com/example/hfh/config/JwtInterceptor.java) | 从请求头取 Token → 验证有效性 → 判断是否过期 |
| 拦截器注册 | [WebConfig.java](jwt-api/src/main/java/com/example/hfh/config/WebConfig.java) | 配置拦截路径（如 `/common/**`） |
| 工具类 | [JWTUtils.java](jwt-api/src/main/java/com/example/hfh/utils/JWTUtils.java) | 封装 Token 生成和解析逻辑 |

**使用方式**：

```java
@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestParam String username, @RequestParam String password) {
        // 1. 验证用户名密码（查数据库）
        // 2. 生成 Token
        Map<String, String> payload = new HashMap<>();
        payload.put("userId", user.getId().toString());
        payload.put("role", user.getRole());
        String token = JWTUtils.generateToken(payload);
        return ApiResponse.success("登录成功", token);
    }

    @JwtToken  // ✅ 加上此注解，该接口需要认证
    @GetMapping("/info")
    public ApiResponse<?> userInfo(HttpServletRequest request) {
        String token = request.getHeader("token");
        DecodedJWT decoded = JWTUtils.resolveToken(token);
        String userId = decoded.getClaim("userId").asString();
        // 根据 userId 查询用户信息...
        return ApiResponse.success(user);
    }
}
```

---

## 统一响应格式

[ApiResponse.java](jwt-api/src/main/java/com/example/hfh/resp/ApiResponse.java) 封装了标准返回结构：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": { ... }
}
```

**使用**：
```java
return ApiResponse.success("查询成功", dataList);
return ApiResponse.failure("参数错误");
return ApiResponse.failure(ApiResponseCode.UNAUTHORIZED); // 使用预定义错误码
```

**全局异常处理**（[GlobalControllerAdvice.java](jwt-api/src/main/java/com/example/hfh/config/GlobalControllerAdvice.java)）：
- 捕获所有 `RuntimeException`，统一返回错误格式
- 前端无需逐个接口处理异常

---

## 快速启动

### 后端

```bash
cd jwt-api
# 1. 修改 application.yml 中的数据库连接信息
# 2. 启动 SpringbootApplication.java (main 方法)
# 3. 访问 http://localhost:9090/doc.html 查看 API 文档
```

### 前端

```bash
cd jwt-ui
npm install
npm run dev
```

### 测试认证流程

```bash
# 1. 登录获取 Token
curl -X POST http://localhost:9090/common/login \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "acc=1&pwd=1"

# 2. 使用 Token 访问受保护接口
curl -X POST http://localhost:9090/common/test \
  -H "token: <上一步返回的token>"
```

---

## 扩展建议

- [ ] 将 SECRET 移至 `application.yml` 配置文件，避免硬编码
- [ ] 引入 Redis 存储 Token 实现主动注销（踢人下线）
- [ ] 使用 RefreshToken 机制实现无感刷新
- [ ] 集成 Spring Security 替代手动拦截器（更完善的权限体系）
- [ ] 前端 Axios 拦截器统一处理 401 跳转登录页
