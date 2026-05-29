# Spring Security + JWT 实战

> Spring Boot 3.3.3 + Spring Security 6 + jjwt 0.11.5，无状态 Token 认证方案。

## 技术选型

| 组件 | 版本 | 用途 |
|------|------|------|
| spring-boot-starter-security | 6.x | 认证授权框架 |
| jjwt (api/impl/jackson) | 0.11.5 | JWT 签发与解析 |
| mybatis-plus-spring-boot3-starter | 3.5.11 | ORM |
| postgresql | runtime | 数据库 |
| knife4j-openapi3 | latest | 接口文档 |
| bcrypt | 内置 | 密码哈希 |

## 架构总览

```
请求 → JwtAuthenticationFilter(OncePerRequestFilter)
     → 解析 Authorization: Bearer <token>
     → SecurityContextHolder 注入 Authentication
     → 后续 FilterChain / Controller 正常执行
```

## 核心配置

### 1. SecurityFilterChain — 安全策略核心

[SecurityConfig.java](src/main/java/com/example/demo/config/SecurityConfig.java)

```java
http.csrf(csrf -> csrf.disable())                                    // 关闭 CSRF（JWT 无需）
   .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))      // 无状态，不创建 Session
   .authorizeHttpRequests(auth -> auth
       .requestMatchers("/api/auth/**").permitAll()                   // 白名单：登录/注册
       .requestMatchers(WHITE_LIST).permitAll()                       // Swagger 文档
       .anyRequest().authenticated()                                   // 其余全部需要认证
   )
   .exceptionHandling(eh -> eh
       .authenticationEntryPoint(authEntryPoint)                       // 未登录 → 401
       .accessDeniedHandler(accessDeniedHandler)                       // 已登录无权限 → 403
   )
   .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // JWT 过滤器插队
```

**要点**：
- `STATELESS` 是 JWT 方案的关键，服务端不存 Session
- `addFilterBefore` 把自定义过滤器插到用户名密码过滤器之前，先校验 Token 再走后续链路
- `@EnableMethodSecurity` 开启方法级权限注解 `@PreAuthorize`

### 2. JwtAuthenticationFilter — Token 拦截器

[JwtAuthenticationFilter.java](src/main/java/com/example/demo/security/JwtAuthenticationFilter.java)

继承 `OncePerRequestFilter`，每个请求只执行一次：

```
取 Header → Bearer 前缀校验 → jwtUtil.isValid() 验签/过期 →
loadUserByUsername 查库 → 构建 UsernamePasswordAuthenticationToken →
塞入 SecurityContextHolder
```

**注意点**：
- Token 无效时不抛异常、不阻断，直接 `filterChain.doFilter()` 放行，让后续逻辑返回 401/403
- 只在 `SecurityContextHolder 为空时` 才设置，避免覆盖已有认证信息

### 3. JwtUtil — JWT 工具类

[JwtUtil.java](src/main/java/com/example/demo/utils/JwtUtil.java)

```java
// 签发
Jwts.builder()
    .setSubject(username)           // payload: 用户名
    .setIssuedAt(now)
    .setExpiration(now + expireMillis)
    .signWith(Keys.hmacShaKeyFor(secret), HS512)
    .compact();

// 解析验签
Jwts.parserBuilder()
    .setSigningKey(Keys.hmacShaKeyFor(secret))
    .build()
    .parseClaimsJws(token);         // 自动校验签名+过期时间
```

### 4. 登录流程

[AuthController.java](src/main/java/com/example/demo/controller/AuthController.java)

```
POST /api/auth/login { username, password }
  → AuthenticationManager.authenticate()
    → DaoAuthenticationProvider 调用 UserDetailsService.loadUserByUsername()
    → BCryptPasswordEncoder.matches() 对比密文
  → 认证成功 → jwtUtil.generateToken(username) → 返回 token
```

**关键**：登录不走 FilterChain，直接用 `AuthenticationManager` 做认证，成功后手动签发 Token。

### 5. 异常处理分级

| 场景 | 触发组件 | HTTP 状态码 | 响应体 |
|------|---------|------------|--------|
| 未登录 / Token 无效 | [JwtAuthenticationEntryPoint](src/main/java/com/example/demo/security/JwtAuthenticationEntryPoint.java) | 401 | `{code:401, message:"未携带或无效 Token"}` |
| 已登录但权限不足 | [JwtAccessDeniedHandler](src/main/java/com/example/demo/security/JwtAccessDeniedHandler.java) | 403 | `{code:403, message:"权限不足"}` |

### 6. UserDetailsService — 用户数据桥接

[JwtUserDetailsService.java](src/main/java/com/example/demo/security/JwtUserDetailsService.java)

```java
UserEntity u = userService.findByUsername(username);
return new User(u.getUsername(), u.getPassword(),
    List.of(new SimpleGrantedAuthority(u.getRole())));  // 角色 → Authority
```

Spring Security 不关心你的表结构，只要 `UserDetailsService` 返回 `UserDetails` 即可。角色字段直接转成 `GrantedAuthority`，配合 `@PreAuthorize("hasRole('ADMIN')")` 使用。

## 项目结构

```
config/
  ├── SecurityConfig.java          # FilterChain + 白名单 + 异常处理
  └── PasswordEncoderConfig.java   # BCrypt Bean
security/
  ├── JwtAuthenticationFilter.java # OncePerRequestFilter，每次请求解析 Token
  ├── JwtUserDetailsService.java   # 桥接数据库用户 ↔ UserDetails
  ├── JwtAuthenticationEntryPoint.java  # 401 处理
  └── JwtAccessDeniedHandler.java       # 403 处理
utils/
  └── JwtUtil.java                 # 签发 / 解析 / 验签
controller/
  └── AuthController.java          # login + register
entity/
  └── UserEntity.java              # username, password, role
service/
  └── UserService.java             # 注册 / 查询
```

## 启动前准备

1. **数据库**：PostgreSQL，建表脚本见 [sql/users.sql](sql/users.sql)
2. **配置**：`application-dev.yml` 中补充 `jwt.secret` 和 `jwt.expire` 及数据源
3. **接口文档**：启动后访问 `/doc.html`（Knife4j），账号密码均为 `1`

## 常见问题

**Q: 为什么不用 Session？**
A: JWT 天然无状态，适合分布式/微服务场景。`SessionCreationPolicy.STATELESS` 禁用 Session 后，Security 完全依赖 Token 判断身份。

**Q: Token 过期怎么办？**
A: 当前实现是简单过期重登。生产环境可加 Refresh Token 机制或缩短有效期 + 前端静默续期。

**Q: 如何加角色权限控制？**
A: 已开启 `@EnableMethodSecurity`，在 Controller 方法上加 `@PreAuthorize("hasRole('ADMIN')")` 即可，角色值来自数据库 `role` 字段。
