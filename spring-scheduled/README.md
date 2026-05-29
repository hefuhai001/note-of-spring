# Spring 定时任务全方案实战

> 从单机到分布式，7 种方案一次讲透

## 项目结构

```
src/main/java/com/example/scheduled/
├── _01_Scheduled/          # @Scheduled 注解
├── _02_ScheduledExecutor/   # ScheduledExecutorService
├── _03_quartz/             # Quartz 调度框架
├── _04_SchedulingConfigurer/ # 动态调度配置
├── _05_TaskScheduler/      # TaskScheduler 配置
├── _06_Redis/              # Redis 分布式锁
└── _07_XXL/                # XXL-JOB 分布式调度
```

---

## 一、@Scheduled（Spring 内置）

**适用场景**：单机简单定时任务

```java
@Scheduled(fixedRate = 5000)  // 固定频率：每5秒执行
public void executeTask() { }

@Scheduled(cron = "0 0 12 * * ?")  // Cron 表达式：每天12点执行
public void executeCronTask() { }
```

**核心参数**：
| 参数 | 说明 | 示例 |
|------|------|------|
| `fixedRate` | 固定频率（上次开始后计时） | `5000` = 5秒 |
| `fixedDelay` | 固定延迟（上次结束后计时） | `5000` = 5秒 |
| `initialDelay` | 首次延迟启动 | `10000` = 10秒 |
| `cron` | Cron 表达式 | `0/5 * * * * ?` |

**⚠️ 注意**：
- 默认**单线程串行执行**，前一个任务没跑完会阻塞后续任务
- 需配合 `SchedulingConfigurer` 配置线程池解决并发问题

**开启方式**：启动类加 `@EnableScheduling`

---

## 二、ScheduledExecutorService（JDK 原生）

**适用场景**：脱离 Spring 容器的纯 Java 定时任务

```java
ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
executor.scheduleAtFixedRate(() -> {
    System.out.println("任务执行：" + System.currentTimeMillis());
}, 0, 5, TimeUnit.SECONDS);  // 初始延迟0秒，之后每5秒执行
```

**三种调度方法对比**：

| 方法 | 特点 |
|------|------|
| `schedule()` | 执行一次，可指定延迟时间 |
| `scheduleAtFixedRate()` | 固定频率（不管上一次是否完成） |
| `scheduleWithFixedDelay()` | 固定延迟（等上一次完成后才开始计时） |

**优势**：线程池管理、支持优雅关闭（`shutdown()`）

---

## 三、Quartz（企业级调度框架）

**适用场景**：复杂调度需求（持久化、集群、错失补偿）

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-quartz</artifactId>
</dependency>
```

**核心概念**：

| 概念 | 说明 |
|------|------|
| **Job** | 要执行的任务（实现 `Job` 接口） |
| **Trigger** | 触发器（定义何时触发） |
| **JobDetail** | Job 的详细定义（携带参数） |
| **Scheduler** | 调度器（管理所有 Job 和 Trigger） |

**使用示例**：

```java
// 定义 Job
public class MyJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("Quartz 任务执行");
    }
}

// 创建 Trigger
Trigger trigger = TriggerBuilder.newTrigger()
    .withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?"))
    .build();
```

**Quartz vs @Scheduled**：

| 特性 | Quartz | @Scheduled |
|------|--------|------------|
| 持久化到数据库 | ✅ 支持 | ❌ 不支持 |
| 集群部署 | ✅ 支持 | ❌ 单机 |
| 错失触发补偿 | ✅ 支持 | ❌ 不支持 |
| 动态增删任务 | ✅ 支持 | ❌ 静态 |
| 复杂度 | 较高 | 极低 |

---

## 四、SchedulingConfigurer（动态调度）

**适用场景**：运行时动态修改 Cron 表达式

```java
@Configuration
public class DynamicSchedulingConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
            () -> System.out.println("动态任务执行"),
            new Trigger() {
                @Override
                public Date nextExecutionTime(TriggerContext triggerContext) {
                    // 可从数据库/配置中心读取最新 Cron
                    return new CronTrigger("0/5 * * * * ?")
                        .nextExecutionTime(triggerContext);
                }
            }
        );
    }
}
```

**典型用法**：
- 从数据库读取 Cron 表达式，实现热更新
- 结合 Nacos/Apollo 配置中心，无需重启应用

---

## 五、TaskScheduler（自定义线程池）

**适用场景**：控制 @Scheduled 的并发能力

```java
@Configuration
@EnableScheduling
public class TaskSchedulerConfig implements SchedulingConfigurer {
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);  // 核心线程数
        executor.initialize();

        taskRegistrar.setTaskExecutor(executor);
    }
}
```

**⚠️ 必须配置的原因**：
- 默认线程池大小为 **1**
- 多个 @Scheduled 任务会互相阻塞
- 生产环境建议设置 `corePoolSize >= 任务数量`

---

## 六、Redis 分布式锁（防重复执行）

**适用场景**：多实例部署时避免重复执行

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

**实现原理**：

```java
@Component
public class RedisDistributedTask {

    @Scheduled(fixedRate = 5000)
    public void executeTask() {
        String lockKey = "distributedTaskLock";
        String lockValue = String.valueOf(System.currentTimeMillis());

        // setIfAbsent = SETNX（不存在才设置）
        Boolean lock = redisTemplate.opsForValue()
            .setIfAbsent(lockKey, lockValue, 5, TimeUnit.SECONDS);

        if (Boolean.TRUE.equals(lock)) {
            System.out.println("分布式任务执行：" + new Date());
        }
    }
}
```

**关键点**：
- `setIfAbsent` + 过期时间 = 原子操作（Redis 2.6.12+）
- 过期时间要大于任务执行时间（防止死锁）
- 生产环境建议用 **Redisson** 或 **RedLock** 替代手写

---

## 七、XXL-JOB（分布式任务调度平台）

**适用场景**：大规模微服务任务调度（推荐生产使用）

```xml
<dependency>
    <groupId>com.xuxueli</groupId>
    <artifactId>xxl-job-core</artifactId>
    <version>3.0.0</version>
</dependency>
```

**核心特性**：
- 📊 **可视化控制台**：任务管理、日志查看、运行报表
- 🔀 **弹性扩容**：自动分片广播，支持海量数据并行处理
- ⏰ **故障转移**：执行器宕机自动路由到其他节点
- 🔄 **失败重试**：支持自定义重试次数和间隔

**使用示例**：

```java
@Component
public class XxlJobExample {

    @XxlJob("myJobHandler")
    public void myJobHandler() {
        System.out.println("XXL-JOB 任务执行：" + System.currentTimeMillis());
    }
}
```

**架构图**：

```
┌─────────────┐     ┌─────────────────┐     ┌─────────────┐
│  调度中心     │────▶│  执行器 (集群)     │────▶│  业务逻辑     │
│  (Admin)    │     │  Executor       │     │  Handler    │
└─────────────┘     └─────────────────┘     └─────────────┘
     │                     │
     │  HTTP/RPC           │  注册/心跳
     └─────────────────────┘
```

**与 Quartz 对比**：

| 特性 | XXL-JOB | Quartz |
|------|---------|--------|
| 控制台 | ✅ Web UI | ❌ 无 |
| 运维成本 | 低 | 高 |
| 分片广播 | ✅ 原生支持 | ❌ 需自行实现 |
| 社区活跃度 | 高（国内主流） | 一般 |

---

## 方案选型指南

```
需求复杂度
    ↑
    │              ┌──────────────┐
    │              │  XXL-JOB     │ ← 微服务/大规模
    │              ├──────────────┤
    │         ┌────┤  Quartz      │ ← 复杂调度/持久化
    │         │    ├──────────────┤
    │    ┌────┴────┤  SchedulingConfigurer │ ← 动态Cron
    │    │         ├──────────────┤
    │    │    ┌────┤  Redis锁+@Scheduled   │ ← 多实例防重复
    │    │    │    ├──────────────┤
    │    │    │    │  @Scheduled  │ ← 简单单机
    │    │    │    ├──────────────┤
    └────┴────┴────┤  ScheduledExecutorService │ ← 纯Java
                   └──────────────┘
                         →
                    学习成本递减
```

**快速决策**：
- 单机简单任务 → **@Scheduled**
- 多实例防重复 → **Redis 锁 / XXL-JOB**
- 复杂调度/需要持久化 → **Quartz**
- 生产环境微服务 → **XXL-JOB**（首选）

---

## 技术栈

- **JDK**: 17
- **Spring Boot**: 3.4.4
- **Quartz**: spring-boot-starter-quartz
- **Redis**: spring-boot-starter-data-redis
- **XXL-JOB**: 3.0.0

## 快速启动

1. 克隆项目并导入 IDE
2. 配置 Redis 连接（如需测试 _06_Redis 模块）
3. 启动 `SpringScheduledApplication`
4. 取消对应模块代码注释即可观察效果
