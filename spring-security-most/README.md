# Spring Security Most - 多表权限认证实战

基于 **Spring Security 6.x + JWT** 的多表用户认证系统。普通用户存 `t_user` 表，管理员存 `t_admin` 表，通过 `username|userType` 格式实现统一入口、分表查询。

## 核心设计：多表认证

### 表结构

```sql
-- 普通用户表
CREATE TABLE t_user (
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(255),
    role     VARCHAR(20) DEFAULT 'ROLE_USER'
);

-- 管理员表
CREATE TABLE t_admin (
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(255),
    role     VARCHAR(20) DEFAULT 'ROLE_ADMIN'
);
```

两套表物理隔离，字段独立扩展。

### 认证流程

```
登录请求: { "username": "zhangsan", "password": "xxx", "type": "user" }
                    ↓
        AuthServiceImpl 拼接为 "zhangsan|user"
                    ↓
        CustomUserDetailsService.loadUserByUsername("zhangsan|user")
                    ↓
            解析 userType = "user"
           /                        \
    userType="admin"            userType="user"
          ↓                          ↓
   adminMapper.findByUsername()  userMapper.findByUsername()
          ↓                          ↓
     t_admin 表查询              t_user 表查询
          ↓                          ↓
     UserDetailsImpl.buildAdmin() UserDetailsImpl.buildUser()
          └──────────┬───────────────┘
                     ↓
            返回 UserDetails (username 字段存储 "zhangsan|user")
                     ↓
            JJWT 生成 Token（subject = "zhangsan|user"）
                     ↓
         客户端携带 Token 访问接口
```

### 关键代码

**1. 统一 UserDetailsService — [CustomUserDetailsService.java](src/main/java/com/example/demo/service/jwt/CustomUserDetailsService.java)**

根据 `username|userType` 路由到不同的 Mapper：

```java
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AdminMapper adminMapper;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String usernameWithType) {
        String[] parts = usernameWithType.split("\\|");
        String username = parts[0];
        String userType = parts[1].toLowerCase();

        if ("admin".equals(userType)) {
            Admin admin = adminMapper.findByUsername(username);
            return UserDetailsImpl.buildAdmin(admin);      // → t_admin
        } else if ("user".equals(userType)) {
            User user = userMapper.findByUsername(username);
            return UserDetailsImpl.buildUser(user);        // → t_user
        }
        throw new UsernameNotFoundException("无效的用户类型");
    }
}
```

**2. UserDetails 包装 — [UserDetailsImpl.java](src/main/java/com/example/demo/service/jwt/UserDetailsImpl.java)**

`username` 字段存储完整格式 `name|type`，后续过滤器可解析：

```java
@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private String username;   // "zhangsan|user" 或 "admin|admin"
    private String password;
    private String role;       // ROLE_USER / ROLE_ADMIN
    private String userType;   // user / admin

    public static UserDetailsImpl buildUser(User user) {
        return new UserDetailsImpl(
            user.getUsername() + "|user",
            user.getPassword(),
            user.getRole(),
            "user"
        );
    }

    public static UserDetailsImpl buildAdmin(Admin admin) {
        return new UserDetailsImpl(
            admin.getUsername() + "|admin",
            admin.getPassword(),
            admin.getRole(),
            "admin"
        );
    }
}
```

**3. 动态权限校验 — [DocumentPermissionChecker.java](src/main/java/com/example/demo/service/impl/DocumentPermissionChecker.java)**

基于 userType 实现细粒度资源控制：

```java
@Service
public class DocumentPermissionChecker {

    public boolean canViewDocument(String usernameWithType, String docId) {
        String[] parts = usernameWithType.split("\\|");
        String username = parts[0];
        String userType = parts[1];

        if ("admin".equalsIgnoreCase(userType)) return true;  // 管理员看所有
        return docId.startsWith(username + "_");               // 用户只能看自己的
    }

    public boolean canEditDocument(String usernameWithType, String docId) {
        String[] parts = usernameWithType.split("\\|");
        String username = parts[0];
        return docId.startsWith(username + "_");               // 只有所有者能编辑
    }
}
```

**4. Security 配置 — [SecurityConfig.java](src/main/java/com/example/demo/config/SecurityConfig.java)**

```java
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(WHITE_LIST).permitAll()
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/test/admin").hasRole("ADMIN")
            .requestMatchers("/api/test/user").hasRole("USER")
            .anyRequest().authenticated()
        )
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authenticationProvider(authenticationProvider())
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
}
```

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.3.3 | 基础框架 |
| Spring Security | 6.x | 多表认证授权 |
| JJWT | 0.11.5 | JWT Token 处理 |
| MyBatis-Plus | 3.5.11 | 双表 ORM |
| Knife4j | 4.5.0 | API 文档 |
| PostgreSQL | 14+ | 数据库 |

## 项目结构

```
src/main/java/com/example/demo/
├── config/
│   ├── SecurityConfig.java              # 权限规则配置
│   ├── JwtAuthenticationFilter.java     # 从 Token 解析 username|userType
│   ├── GlobalExceptionHandler.java
│   └── WebMvcConfig.java
├── controller/
│   ├── AuthController.java              # 登录（接收 type 参数区分表）
│   ├── UserController.java
│   ├── AdminController.java
│   └── CommonController.java
├── service/
│   ├── AuthService.java / impl/
│   ├── impl/DocumentPermissionChecker.java  # 动态权限（按 userType 判断）
│   └── jwt/
│       ├── JwtService.java              # Token 签发/验证
│       ├── CustomUserDetailsService.java   # ★ 核心：按 userType 路由 Mapper
│       └── UserDetailsImpl.java         # ★ 存储 name|type 格式
├── entity/
│   ├── User.java                        # → t_user
│   ├── Admin.java                       # → t_admin
│   └── dto/                             # LoginRequest 含 type 字段
├── mapper/
│   ├── UserMapper extends BaseMapper<User>
│   └── AdminMapper extends BaseMapper<Admin>
sql/
├── t_user.sql                           # 普通用户表
└── t_admin.sql                          # 管理员表
```

## 三方库详解

### JJWT 0.11.5 — Token 中携带多表信息

[JwtService.java](src/main/java/com/example/demo/service/jwt/JwtService.java) 将 `username|userType` 写入 Token subject：

```java
public String generateToken(UserDetails userDetails) {
    return Jwts.builder()
            .subject(userDetails.getUsername())  // subject = "zhangsan|user"
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(getSignInKey(), Jwts.SIG.HS256)
            .compact();
}

public String extractUsername(String token) {
    return Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();  // 取出 "zhangsan|user"
}
```

**配置：**
```yaml
jwt:
  secret: "${JWT_SECRET:your-secret-key}"
  expire: 3600000
```

### MyBatis-Plus 3.5.11 — 双表操作

两个 Mapper 各自继承 BaseMapper，独立 CRUD：

```java
@Mapper
public interface UserMapper extends BaseMapper<User> { }

@Mapper
public interface AdminMapper extends BaseMapper<Admin> { }
```

自定义查询方法：
```java
// AdminMapper.java
Admin findByUsername(@Param("username") String username);

// UserMapper.java
User findByUsername(@Param("username") String username);
```

### Knife4j 4.5.0

访问 `http://localhost:8080/doc.html`（账号密码 `1/1`）

## 快速开始

**环境：** JDK 17+ / Maven 3.8+ / PostgreSQL 14+

```bash
# 1. 建表
psql -U postgres -f sql/t_user.sql
psql -U postgres -f sql/t_admin.sql

# 2. 修改 application.yml 数据库连接

# 3. 启动
mvn spring-boot:run
```

## API 接口

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| POST | `/api/auth/register` | 注册（body 含 type: user/admin） | 公开 |
| POST | `/api/auth/login` | 登录（body 含 type: user/admin） | 公开 |
| GET | `/api/user/info` | 用户信息 | USER |
| GET | `/api/admin/users` | 用户列表 | ADMIN |
| GET | `/api/common/hello` | 公开接口 | 无需认证 |

**登录请求示例：**
```json
// 普通用户登录
POST /api/auth/login
{ "username": "zhangsan", "password": "123456", "type": "user" }

// 管理员登录
POST /api/auth/login
{ "username": "admin", "password": "123456", "type": "admin" }
```

**响应（返回 JWT）：**
```json
{ "token": "eyJhbGciOiJIUzI1NiJ9..." }
```

**后续请求携带：**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## 多表认证 vs 单表对比

| 维度 | 单表（role 字段） | 多表（本项目方案） |
|------|------------------|-------------------|
| 表设计 | 一张 users 表加 role 字段 | t_user + t_admin 物理隔离 |
| 字段扩展 | 不同角色共用字段，大量 NULL | 各表字段独立，结构清晰 |
| 数据安全 | SQL 注入可能跨角色读取数据 | 表级隔离，天然安全 |
| 查询性能 | 需要 WHERE role=? 过滤 | 直接定位目标表，无额外过滤 |
| 适用场景 | 角色简单、字段相同 | 角色差异大、字段不同 |
| 扩展性 | 新增角色需加枚举值 | 新增角色只需新建表 + Mapper |

## 生产环境检查清单

- [ ] `jwt.secret` 使用环境变量注入，不硬编码
- [ ] 启用 HTTPS
- [ ] CORS 配置白名单
- [ ] 定期更新依赖版本
- [ ] Token 黑名单机制（Redis/JWT version）
