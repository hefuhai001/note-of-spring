# Spring Security 多用户体系 + JWT 无状态认证

> 基于 Spring Boot 3.3.3 + Spring Security 6 + jjwt，实现 **User / Admin 双表独立存储**的统一认证与鉴权方案。

---

## 技术栈

| 组件 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.3.3 | 基础框架 |
| Spring Security | 6.x | 认证鉴权 |
| jjwt | 0.11.5 | JWT 签发与解析 |
| MyBatis-Plus | 3.5.11 | ORM |
| Knife4j | 4.5.0 | Swagger 增强 UI |
| PostgreSQL | — | 数据库 |

---

## 核心设计

### 双表用户体系

```
t_user (id, username, password, role)
t_admin(id, username, password, role)
```

两张表结构一致但物理隔离，通过 `CompositeUserDetailsService` 统一查询：

```java
// 先查 User → 再查 Admin → 都没有抛异常
User user = userMapper.selectOne(...);
if (user != null) return buildUserDetails(user);

Admin admin = adminMapper.selectOne(...);
if (admin != null) return buildUserDetails(admin);

throw new UsernameNotFoundException("用户名不存在");
```

### 认证流程

```
客户端                    服务端
  │                        │
  ├── POST /login/user ──→ │ AuthenticationManager.authenticate()
  │                        │   ↓
  │                        │ CompositeUserDetailsService.loadUserByUsername()
  │                        │   ↓ (密码校验通过)
  │←── JWT Token ─────────│ JwtUtil.generateToken(username)
  │                        │
  ├── GET /api/user/xxx ──→ │ JwtAuthenticationFilter
  │   Header: Bearer xxx   │   ↓ 解析 Token → 设置 SecurityContext
  │←── 业务数据 ──────────│ Controller (需 ROLE_USER)
```

### 权限规则

| 路径 | 权限 |
|------|------|
| `/api/auth/login/*`, `/api/auth/register/*` | 公开 |
| `/api/user/**` | `ROLE_USER` |
| `/api/admin/**` | `ROLE_ADMIN` |
| 其他 | 已认证即可 |

### 无状态配置

```java
.sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
.csrf(csrf -> csrf.disable())
.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
```

- 关闭 Session，完全依赖 JWT
- 关闭 CSRF（无 Cookie 场景不需要）
- 自定义 Filter 在 `UsernamePasswordAuthenticationFilter` 之前执行

---

## API 接口

### 登录

```bash
# 用户登录
POST /api/auth/login/user
Content-Type: application/json

{"username": "zhangsan", "password": "123456"}

# 管理员登录
POST /api/auth/login/admin
Content-Type: application/json

{"username": "admin", "password": "admin123"}
```

**响应**

```json
{
  "code": 200,
  "data": "eyJhbGciOiJIUzUxMiJ9..."
}
```

### 注册

```bash
POST /api/auth/register/user    # 注册用户
POST /api/auth/register/admin   # 注册管理员
```

### 携带 Token 访问

```bash
GET /api/user/profile
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9...
```

---

## 异常处理

| 场景 | 处理器 | HTTP 状态码 |
|------|--------|-------------|
| 未登录 / Token 无效 | `JwtAuthenticationEntryPoint` | 401 |
| 已登录但无权限 | `JwtAccessDeniedHandler` | 403 |

---

## 配置说明

```yaml
jwt:
  secret: "你的密钥(≥512位用于HS512)"
  expire: 3600000          # Token 有效期（毫秒）

knife4j:
  enable: true
  basic:
    enable: true           # 文档页需要账号密码
    username: 1
    password: ${DB_PASSWORD}
```

启动后访问 `http://localhost:8080/doc.html` 查看 API 文档。

---

## 项目结构

```
src/main/java/com/example/demo/
├── config/
│   ├── SecurityConfig.java              # SecurityFilterChain + AuthenticationManager
│   └── PasswordEncoderConfig.java       # BCrypt 密码加密
├── security/
│   ├── JwtAuthenticationFilter.java     # OncePerRequestFilter 解析 JWT
│   ├── JwtAuthenticationEntryPoint.java # 401 处理
│   └── JwtAccessDeniedHandler.java      # 403 处理
├── service/
│   └── CompositeUserDetailsService.java # 双表查询统一入口
├── controller/
│   ├── AuthController.java              # 登录接口
│   └── RegisterController.java          # 注册接口
├── utils/
│   └── JwtUtil.java                     # JWT 签发/解析/校验
├── entity/
│   ├── User.java
│   └── Admin.java
└── mapper/
    ├── UserMapper.java
    └── AdminMapper.java
```

---

## 关键依赖版本速查

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.3.3</version>
</parent>

<!-- MyBatis-Plus for Spring Boot 3 -->
<dependency>
    <groupId>com.baomidou</groupId>
    <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    <version>3.5.11</version>
</dependency>

<!-- JJWT 三件套 -->
<dependency><groupId>io.jsonwebtoken</groupId><artifactId>jjwt-api</artifactId><version>0.11.5</version></dependency>
<dependency><groupId>io.jsonwebtoken</groupId><artifactId>jjwt-impl</artifactId><version>0.11.5</version><scope>runtime</scope></dependency>
<dependency><groupId>io.jsonwebtoken</groupId><artifactId>jjwt-jackson</artifactId><version>0.11.5</version><scope>runtime</scope></dependency>

<!-- Knife4j for Jakarta (Spring Boot 3) -->
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
    <version>4.5.0</version>
</dependency>
```

> ⚠️ Spring Boot 3 使用 Jakarta 命名空间，Knife4j 必须选 `-jakarta-` 版本。
