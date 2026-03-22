## 动态定时任务项目

### 📋 项目架构概览

这个项目实现了一个**运行时动态创建、取消定时任务**的系统，包含三层结构：

```
Controller层 → Service层 → Task层
```

---

### 🔍 核心原理详解

#### 1. **核心组件：`TaskScheduler`**

```java
private final TaskScheduler taskScheduler;
```

`TaskScheduler` 是 Spring 提供的任务调度器接口，它允许在**运行时**动态调度任务，而不是使用 `@Scheduled` 注解在编译时固定。

#### 2. **任务存储机制**

```java
private final Map<String, ScheduledFuture<?>> taskFutures = new ConcurrentHashMap<>();
private final Map<String, String> taskMessages = new ConcurrentHashMap<>();
```

| 数据结构 | 作用 | 为什么用 ConcurrentHashMap |
|---------|------|---------------------------|
| `taskFutures` | 存储任务ID与任务执行句柄的映射 | 线程安全，支持并发创建/取消任务 |
| `taskMessages` | 存储任务ID与任务描述信息 | 便于查询任务列表 |

#### 3. **创建任务的核心流程**

```
┌─────────────────────────────────────────────────────────────┐
│                    createTask() 方法流程                      │
├─────────────────────────────────────────────────────────────┤
│  1. cancelTask(taskId)     → 取消已存在的同名任务（幂等性）    │
│  2. 计算延迟时间 delay      → 触发时间 - 当前时间              │
│  3. 验证 delay >= 0        → 确保不是过去的时间               │
│  4. taskScheduler.schedule() → 调度任务执行                   │
│  5. 保存 ScheduledFuture   → 存入 taskFutures 便于后续取消    │
└─────────────────────────────────────────────────────────────┘
```

**关键代码解析：**

```java
ScheduledFuture<?> future = taskScheduler.schedule(() -> {
    System.out.println("执行定时任务: " + message + "，时间: " + LocalDateTime.now());
    taskFutures.remove(taskId);  // 执行完自动清理
    taskMessages.remove(taskId);
}, new Date(System.currentTimeMillis() + delay));
```

这里 `taskScheduler.schedule()` 接收两个参数：
- **第一个参数**：`Runnable` 任务逻辑（Lambda表达式）
- **第二个参数**：`Date` 类型的触发时间

#### 4. **取消任务的原理**

```java
public void cancelTask(String taskId) {
    ScheduledFuture<?> future = taskFutures.remove(taskId);
    if (future != null) {
        future.cancel(true);  // true表示中断正在执行的任务
    }
    taskMessages.remove(taskId);
}
```

`ScheduledFuture.cancel(true)` 会：
- 如果任务还未执行 → 取消执行
- 如果任务正在执行 → 尝试中断线程

---

### 🆚 对比：动态任务 vs 静态任务

| 特性 | `@Scheduled` 注解 | `TaskScheduler` 动态调度 |
|------|------------------|------------------------|
| 配置时机 | 编译时固定 | 运行时动态 |
| 触发时间 | 固定/表达式 | 可任意指定 |
| 任务取消 | 不支持 | 支持 |
| 任务管理 | 无状态 | 可追踪、可管理 |

---

### 📊 完整调用链路

```
HTTP POST /schedule/create
        ↓
ScheduleController.createTask()
        ↓
ScheduleService.createTask()
        ↓
DynamicTask.createTask()
        ↓
TaskScheduler.schedule() → 线程池调度
        ↓
ScheduledFuture 保存到 Map
        ↓
到达触发时间 → 执行任务逻辑 → 自动清理
```

---

### ⚠️ 注意事项

1. **线程池配置**：默认使用单线程，生产环境建议配置线程池
2. **任务持久化**：当前实现是内存存储，重启后任务丢失
3. **集群问题**：多实例部署时任务会重复执行，需要分布式锁或分布式任务调度框架

这个实现展示了 Spring 任务调度的核心机制，是理解更复杂任务调度框架（如 Quartz、XXL-Job）的基础。