# Spring AOP 实战：自定义注解实现操作日志

## 核心依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId> <!-- 自动包含 AOP -->
</dependency>
```

Spring Boot 2.x/3.x 的 `spring-boot-starter-web` 已内置 AspectJ，无需额外引入 `spring-boot-starter-aop`。

---

## 实现原理

### 1. 自定义注解

[Log.java](src/main/java/com/example/hfh/utils/Log.java)

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    String operation() default "";  // 操作描述
}
```

**要点**：
- `@Target(METHOD)` — 只能标注在方法上
- `@Retention(RUNTIME)` — 运行时反射读取，必须选 RUNTIME

### 2. 切面定义

[LogAspect.java](src/main/java/com/example/hfh/utils/LogAspect.java)

```java
@Aspect
@Component
public class LogAspect {

    // 切点：匹配所有带 @Log 注解的方法
    @Pointcut("@annotation(com.example.hfh.utils.Log)")
    public void logPointCut() {}

    // 环绕通知：在方法执行前后插入逻辑
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();  // 执行目标方法
        long timeTaken = System.currentTimeMillis() - startTime;

        // 获取注解属性
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Log logAnnotation = signature.getMethod().getAnnotation(Log.class);

        // 获取请求信息（仅限 Web 环境）
        HttpServletRequest request = 
            ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();

        // 构建日志对象并持久化
        SysOperLog log = new SysOperLog();
        log.setOperation(logAnnotation.operation());
        log.setMethod(signature.getName());
        log.setIp(request.getRemoteAddr());
        logService.saveLog(log);

        return result;
    }
}
```

**核心 API 说明**：

| 对象 | 用途 |
|------|------|
| `ProceedingJoinPoint` | 环绕通知专用，可获取目标方法签名、参数，调用 `proceed()` 执行原方法 |
| `MethodSignature` | 获取方法元信息（方法名、参数类型、注解等） |
| `RequestContextHolder` | 从 ThreadLocal 获取当前 HTTP 请求（Controller 层有效） |
| `joinPoint.getArgs()` | 获取目标方法的入参数组 |

### 3. 使用方式

[UserController.java](src/main/java/com/example/hfh/controller/UserController.java)

```java
@RestController
public class UserController {

    @Log(operation = "查询用户信息")
    @GetMapping("/list")
    public String list() {
        return "User Data";
    }
}
```

只需一个注解，自动记录操作日志。

---

## AOP 五种通知对比

| 注解 | 执行时机 | 能否修改返回值 | 典型场景 |
|------|----------|----------------|----------|
| `@Before` | 方法执行前 | ❌ | 参数校验、权限检查 |
| `@After` | 方法执行后（无论成功异常） | ❌ | 资源释放 |
| `@AfterReturning` | 方法正常返回后 | ❌ | 结果日志、缓存更新 |
| `@AfterThrowing` | 方法抛出异常时 | ❌ | 异常告警、事务回滚 |
| **`@Around`** | **包裹整个方法** | ✅ | **性能监控、日志记录、缓存拦截** |

本项目使用 `@Around`，因为它能同时拿到方法执行前后的上下文。

---

## 切点表达式语法速查

```
// 匹配特定注解（本例用法）
@annotation(com.example.hfh.utils.Log)

// 匹配某个包下所有方法
execution(* com.example.hfh.controller.*.*(..))

// 匹配返回值、类名、方法名、参数
execution(public * com.example..*Service.find*(String, ..))

// 组合条件：且 / 或 / 非
@annotation(Log) && execution(* com.example..*(..))
within(com.example.controller.*) || within(com.example.service.*)
!@annotation(NoLog)
```

**通配符含义**：
- `*` — 匹配任意字符（不含 .）
- `..` — 匹配任意层包或任意个参数
- `+` — 匹配子类

---

## 数据库设计

[SysOperLog.java](src/main/java/com/example/hfh/entity/SysOperLog.java) → [db_aop.sql](sql/db_aop.sql)

```sql
CREATE TABLE sys_oper_log (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    operation   VARCHAR(255) NOT NULL COMMENT '操作描述',
    method      VARCHAR(255) COMMENT '方法名',
    params      TEXT         COMMENT '请求参数(JSON)',
    ip          VARCHAR(50)  COMMENT '操作IP',
    create_time DATETIME     COMMENT '操作时间'
);
```

---

## 项目结构

```
spring-aop/
├── src/main/java/com/example/aop/
│   ├── utils/
│   │   ├── Log.java          # 自定义注解
│   │   └── LogAspect.java    # AOP切面（核心）
│   ├── entity/
│   │   └── SysOperLog.java   # 日志实体
│   ├── service/
│   │   └── LogService.java   # 日志持久化
│   └── controller/
│       └── UserController.java # 使用示例
├── sql/db_aop.sql            # 建表脚本
└── pom.xml                   # Spring Boot 3.4.4 + JPA + MySQL
```

---

## 常见问题

### Q: `RequestContextHolder` 为 null？
A: 非线程环境（如定时任务、消息消费）中无法获取 HttpServletRequest。解决方案：
- 切面中判断 `RequestContextHolder.getRequestAttributes()` 是否为 null
- 或改用 `@Before` + `JoinPoint` 仅记录方法级信息

### Q: 如何过滤敏感参数？
A: 在 `joinPoint.getArgs()` 后对参数进行脱敏处理，或使用 `@JsonIgnore` 注解。

### Q: AOP 不生效？
检查清单：
1. 切面类是否加 `@Component`
2. 是否在同一 Spring Context（避免父子容器问题）
3. 目标方法是否为 public
4. 是否为同类内部调用（AOP 代理不拦截 self-call）

---

## 技术栈

- **Spring Boot 3.4.4** + Java 17
- **Spring AOP** (AspectJ)
- **Spring Data JPA** + MySQL 8.x
- **Lombok**
