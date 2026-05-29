# Spring Boot 3 + Sa-Token 权限系统实战

## 技术栈

| 组件 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.5.6 | 基础框架 |
| Sa-Token | 1.40.0 | 权限认证 |
| sa-token-redis-jackson | 1.40.0 | 分布式会话存储 |
| MyBatis-Plus | 3.5.7 | ORM |
| Knife4j | 4.5.0 | API 文档 |

## 为什么选 Sa-Token？

对比 Spring Security：

| 维度 | Spring Security | Sa-Token |
|------|-----------------|----------|
| 学习成本 | 高（Filter链复杂） | 低（API 直觉化） |
| 代码量 | 多（配置繁琐） | 少（开箱即用） |
| 灵活性 | 一般 | 高（可插拔） |
| 适用场景 | 企业级复杂权限 | 中小型项目快速落地 |

**一句话总结**：Sa-Token 用 20% 的代码量实现 Security 80% 的功能。

## 核心架构

```
┌─────────────────────────────────────────────┐
│              Controller 层                   │
│  @SaCheckLogin / @SaCheckPermission         │
│  @SaCheckRole                               │
├─────────────────────────────────────────────┤
│          SaInterceptor (全局拦截)             │
│  排除: /auth/login, /doc.html, /swagger/**   │
│  拦截: /** → StpUtil.checkLogin()           │
├─────────────────────────────────────────────┤
│         StpInterfaceImpl (权限查询)           │
│  getPermissionList() → 数据库                │
│  getRoleList() → 数据库                      │
├─────────────────────────────────────────────┤
│              RBAC 数据模型                    │
│  User → UserRole → Role → RolePermission     │
│                       → Permission           │
└─────────────────────────────────────────────┘
```

## 数据库设计（5张表）

```sql
-- 用户表
t_user (id, username, password)

-- 角色表  
t_role (id, role_name)

-- 权限码表
t_permission (id, permission_code)  -- 如: user:add, user:delete

-- 用户-角色关联
t_user_role (user_id, role_id)

-- 角色-权限关联
t_role_permission (role_id, permission_id)
```

**测试数据**：
- `zhang/123456` → admin 角色（拥有 user:add/delete/query）
- `li/123456` → common 角色（仅拥有 user:add）

## 关键配置

### application.yml

```yaml
sa-token:
  token-name: sa-token       # Header/Param 携带 token 的名称
  timeout: 2592000           # Token 有效期 30 天
  is-share: true             # 同端互踢（false 允许多处登录）
  token-style: uuid          # Token 格式: uuid/simple-uuid/random-32...
  is-concurrent: true        # 同账号多端并发登录
```

### Redis 配置（必须）

```yaml
spring:
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      lettuce:
        pool:
          max-active:200
```

> **生产环境必须配 Redis**，否则重启服务后所有 Token 失效。本项目使用 `sa-token-redis-jackson` 序列化方案。

## 三种鉴权方式

### 1️⃣ 全局拦截器（[SaTokenConfigure.java](src/main/java/com/example/demo/config/SaTokenConfigure.java)）

```java
registry.addInterceptor(new SaInterceptor(handler -> {
    SaRouter.match("/**")
        .notMatch("/auth/login", "/doc.html", ...)  // 白名单
        .check(r -> StpUtil.checkLogin());            // 其余需登录
})).addPathPatterns("/**");
```

**适用场景**：统一要求登录的接口

### 2️⃣ 注解鉴权（细粒度控制）

```java
// 仅需登录
@SaCheckLogin
@GetMapping("/info")
public SaResult info() { ... }

// 需要特定权限码
@SaCheckPermission("user:add")
@PostMapping("/add")
public SaResult add() { ... }

// 需要特定角色
@SaCheckRole("admin")
@GetMapping("/list")
public SaResult list() { ... }
```

**适用场景**：不同接口需要不同权限级别

### 3️⃣ 代码手动校验

```java
if (!StpUtil.isLogin()) throw new RuntimeException("未登录");
StpUtil.checkPermission("user:delete");  // 无权限抛异常
StpUtil.checkRole("admin");
```

**适用场景**：业务逻辑中的动态权限判断

## 权限查询实现（核心）

[StpInterfaceImpl.java](src/main/java/com/example/demo/config/StpInterfaceImpl.java) 实现 `StpInterface` 接口：

```java
@Component
public class StpInterfaceImpl implements StpInterface {

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 根据 userId 查询该用户所有权限码
        return rolePermissionMapper.listPermissionByUserId(userId);
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 根据 userId 查询该用户所有角色
        return userRoleMapper.listRoleByUserId(userId);
    }
}
```

**执行流程**：
1. 调用 `@SaCheckPermission("user:add")`
2. Sa-Token 自动调用 `getPermissionList(loginId)`
3. 返回的列表中包含 `"user:add"` → 放行
4. 不包含 → 抛出 `NotPermissionException`

## API 接口清单

| 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|
| POST | `/auth/login` | 无 | 登录，返回 Token |
| POST | `/auth/logout` | 登录 | 登出 |
| GET | `/auth/isLogin` | 登录 | 检查登录状态 |
| POST | `/userEntity/add` | `user:add` | 添加用户 |
| DELETE | `/userEntity/{id}` | `user:delete` | 删除用户 |
| GET | `/userEntity/list` | `admin` 角色 | 用户列表 |
| GET | `/userEntity/listP?userId=1` | 登录 | 查询用户权限 |
| GET | `/userEntity/listR?userId=1` | 登录 | 查询用户角色 |

## 异常统一处理

[ExceptionHandle.java](src/main/java/com/example/demo/config/ExceptionHandle.java)：

| 异常类型 | HTTP状态码 | 场景 |
|----------|-----------|------|
| `NotLoginException` | 401 | 未登录访问受保护接口 |
| `NotPermissionException` | 403 | 缺少权限码 |
| `NotRoleException` | 403 | 缺少角色 |

## 快速启动

### 1. 初始化数据库

```bash
mysql -u root -p < sql/db_sa_token.sql
```

### 2. 启动 Redis

```bash
redis-server
```

### 3. 修改配置

编辑 [application.yml](src/main/resources/application.yml)：
- `spring.datasource.username/password`
- `spring.data.redis.password`（如果有）

### 4. 启动项目

```bash
mvn spring-boot:run
```

### 5. 访问文档

打开浏览器：`http://localhost:8080/doc.html`

## 测试用例

### 登录获取 Token

```bash
curl -X POST http://localhost:8080/auth/login \
  -d "username=zhang&password=123456"
# 返回: {"code":200,"data":"xxxx-uuid-token","msg":"ok"}
```

### 携带 Token 访问接口

```bash
curl http://localhost:8080/userEntity/list \
  -H "sa-token: xxxx-uuid-token"
# 返回: {"code":200,"data":"用户列表","msg":"ok"}
```

### 权限不足测试

```bash
# 用 li 账号登录（common 角色，无 admin）
curl -X POST http://localhost:8080/auth/login \
  -d "username=li&password=123456"

curl http://localhost:8080/userEntity/list \
  -H "sa-token: li的token"
# 返回: {"code":403,"msg":"缺少角色：admin"}
```

## 常见问题

**Q: Token 存在哪里？**
A: 配置了 Redis 就存 Redis，没配置存内存（JVM 重启丢失）。

**Q: 如何实现踢人下线？**
```java
StpUtil.kickout(userId);          // 踢指定用户下线
StpUtil.kickout(userId, device);  // 踢指定设备
```

**Q: 如何获取当前登录用户 ID？**
```java
Object loginId = StpUtil.getLoginId();
Long userId = Long.valueOf(loginId.toString());
```

**Q: 密码应该明文存吗？**
A: **绝对不行！** 示例中明文仅为演示，生产环境用 BCrypt 加密：
```java
// 存入时
String encoded = new BCryptPasswordEncoder().encode(rawPassword);
// 校验时
boolean matches = new BCryptPasswordEncoder().matches(rawPassword, encoded);
```

## 项目结构

```
src/main/java/com/example/demo/
├── config/
│   ├── SaTokenConfigure.java      # 拦截器配置
│   ├── StpInterfaceImpl.java      # 权限查询实现（核心）
│   └── ExceptionHandle.java       # 全局异常处理
├── controller/
│   ├── AuthController.java        # 认证接口（登录/登出）
│   ├── UserController.java        # 用户接口（含权限注解示例）
│   └── ...
├── entity/                        # 实体类
├── mapper/                        # MyBatis Mapper
├── service/                       # 业务逻辑层
└── SpringSaTokenApplication.java  # 启动类
```

## 扩展方向

- [ ] 集成 JWT（无状态认证）
- [ ] 多端登录限制（同账号最多 N 个设备）
- [ ] 按钮级权限控制（前端根据权限码显隐按钮）
- [ ] 数据权限（行级数据隔离）
- [ ] OAuth2 第三方登录集成
- [ ] Gateway 网关统一鉴权
