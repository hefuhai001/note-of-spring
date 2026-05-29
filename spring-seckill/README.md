# Spring Seckill — Redis Lua 秒杀系统

> 基于 Spring Boot 3.5 + Redis Lua 脚本的轻量级秒杀方案，核心就一件事：**用 Lua 保证库存扣减的原子性，用 Redis List 做异步落库削峰**。

## 技术栈与依赖

| 库 | 版本 | 用在哪 |
|---|---|---|
| `spring-boot-starter-data-redis` | 3.5.3 | 全局 Redis 操作入口 |
| `spring-boot-starter-web` | 3.5.3 | 暴露 `/order/create` 接口 |
| `knife4j-openapi3-jakarta-spring-boot-starter` | 4.5.0 | 接口文档（OpenAPI 3），访问 `doc.html` 即可 |

## 架构一句话

```
请求 → Lua脚本原子扣库存 → 成功则订单入Redis List → @Scheduled定时消费落DB
```

## 核心第三方库用法

### 1. spring-data-redis：`StringRedisTemplate` + `RedisScript`

这是整个项目的灵魂。所有库存操作不经过 Java 层判断，全部下沉到 Redis 端执行。

**为什么用 Lua 而不是 `DECR` / `WATCH`？**
- `DECR` 能减到负数，无法区分"刚好卖完"和"超卖"
- `WATCH` 是乐观锁，高并发下大量重试，吞吐量暴跌
- Lua 在 Redis 服务端**单线程顺序执行**，天然原子，一次网络往返搞定「读-判-写」

```lua
-- redis/stockDeduct.lua
local stock = tonumber(redis.call('GET', KEYS[1]))
if (not stock) then return -1 end           -- 商品不存在
if (stock < tonumber(ARGV[1])) then return -2 end  -- 库存不足
redis.call('DECRBY', KEYS[1], ARGV[1])
return stock - tonumber(ARGV[1])            -- 返回剩余库存
```

Java 侧调用方式：

```java
// RedisScriptConfig.java — 将 .lua 文件注册为 Bean
@Bean
public RedisScript<Long> stockScript() {
    DefaultRedisScript<Long> script = new DefaultRedisScript<>();
    script.setLocation(new ClassPathResource("redis/stockDeduct.lua"));
    script.setResultType(Long.class);
    return script;
}

// SecKillService.java — 执行脚本
public boolean deductStock(String sku, int num) {
    Long left = redisTemplate.execute(
            stockScript,
            Collections.singletonList("stock:sku:" + sku),  // KEYS[1]
            String.valueOf(num));                            // ARGV[1]
    return left != null && left >= 0;
}
```

**返回值语义**：
| 返回值 | 含义 |
|---|---|
| `>= 0` | 扣减成功，值为剩余库存 |
| `-1` | 商品 key 不存在 |
| `-2` | 库存不足 |

### 2. Redis List 做异步订单队列

扣库存成功后不同步写数据库，而是往 Redis List 里塞一条消息，后台定时拉取落库：

```java
// Controller — 生产者
redisTemplate.opsForList().leftPush("order:queue", STR."\{orderId}:\{sku}:\{num}");

// OrderConsumer — 消费者，100ms 轮询一次
@Scheduled(fixedDelay = 100)
public void consume() {
    String data = redisTemplate.opsForList().rightPop("order:queue");
    if (data != null) {
        // 解析 → 写 DB（建议此处再带乐观锁做二次校验）
    }
}
```

**为什么不用 RabbitMQ / Kafka？**
- 单机场景 Redis List 够用，省掉额外中间件部署
- `RPUSH` + `RPOP` 天然 FIFO，且 Redis 持久化开启后消息不丢
- 真正上生产再替换为 MQ 即可，接口层无需改动

### 3. Knife4j 4.5.0 — 零配置接口文档

引入依赖后启动项目，浏览器打开 `http://localhost:8080/doc.html` 即可看到自动生成的 API 文档，支持在线调试。

相比原生 Swagger 3，Knife4j 的优势：
- 中文界面，更符合国内习惯
- 支持 `@Tag` / `@Operation` 注解增强分组和描述
- 导出 Markdown / OpenAPI JSON 一键搞定

### 4. CommandLineRunner — 启动时初始化库存

```java
@Component
public class StockInitRunner implements CommandLineRunner {
    @Override
    public void run(String... args) {
        redisTemplate.opsForValue().set("stock:sku:1001", "100");
    }
}
```

应用启动完成后立即执行，适合预加载热点数据到 Redis。

## 启动方式

```bash
# 确保 Redis 已启动在 127.0.0.1:6379
mvn spring-boot:run
```

然后访问 `http://localhost:8080/doc.html` 测试下单接口。

## 可优化方向（未实现）

- [ ] Redisson 分布式锁防止机器时钟跳跃导致重复消费
- [ ] 用户限流（Redis + Lua 实现 token bucket 或滑动窗口）
- [ ] 订单队列消费者改为多线程或独立微服务
- [ ] 库存预热 + 多级缓存（本地 Caffeine + Redis）
