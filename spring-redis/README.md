# Spring Data Redis 实战

Spring Boot 3.1.4 + Jedis 3.6.0 + Spring Data Redis，涵盖 Redis 五大基础数据类型的 Java 操作。

## 技术栈

| 组件 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.1.4 | 基础框架 |
| spring-boot-starter-data-redis | - | Spring Redis 抽象层 |
| jedis | 3.6.0 | Redis Java 客户端 |

## 快速启动

```bash
# 1. 本地启动 Redis 服务（默认 6379 端口）
# 2. 修改 application.yml 中的密码配置
# 3. 启动 SpringRedisApplication
```

连接配置 ([application.yml](src/main/resources/application.yml))：

```yaml
spring:
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password:          # 按需填写
      database: 0
      timeout: 1000
      jedis:
        pool:
          max-active: 8
          max-wait: 5000
          max-idle: 8
          min-idle: 0
```

---

## 一、Jedis 原生操作

### 1.1 基本连接与 Key 操作

[test01.java](src/main/java/com/example/redis/_demo/test01.java) — 直连模式

```java
Jedis jedis = new Jedis("127.0.0.1", 6379);
jedis.auth("password");

jedis.keys("*")              // 获取所有 key
jedis.set("k1", "v1")        // 设置键值对
jedis.del("k1", "k2")        // 删除 key，返回删除个数
jedis.exists("k2")           // 判断 key 是否存在
jedis.close();               // 关闭连接
```

### 1.2 String 类型操作

[test02.java](src/main/java/com/example/redis/_demo/test02.java)

```java
jedis.set("k1", "1")
jedis.get("k1")              // 获取值 → "1"
jedis.setnx("k8", "ldh")     // key 不存在才写入，返回 1=成功 / 0=已存在
jedis.setex("k5", 30, "v5")  // 写入并设 30 秒过期
jedis.incr("k1")             // 原子自增 → 2（点赞/计数场景）
```

> `setnx` + `setex` 组合 = 分布式锁的核心原语；`incr` 是计数器、限流的基础操作。

### 1.3 Hash 类型操作

[test03.java](src/main/java/com/example/redis/_demo/test03.java) — 存储对象的首选结构

```java
jedis.hget("myhash", "name")       // 获取单个 field
jedis.hgetAll("myhash")            // 获取所有 field-value → Map
jedis.hkeys("myhash")              // 获取所有 field 名
jedis.hvals("myhash")              // 获取所有 value
```

### 1.4 连接池

[test04.java](src/main/java/com/example/redis/_demo/test04.java) — 生产环境必须用池化

```java
JedisPoolConfig config = new JedisPoolConfig();
config.setMaxTotal(2000);           // 最大连接数
config.setMaxIdle(10);              // 最大空闲
config.setMinIdle(5);               // 最小空闲
config.setMaxWaitMillis(6000);      // 借用最大等待 ms
config.setTestOnBorrow(true);       // 借出时检测可用性

JedisPool pool = new JedisPool(config, "127.0.0.1", 6379, 1000, "password");
Jedis jedis = pool.getResource();   // 从池中取连接，用完自动归还
```

---

## 二、Spring Data Redis 操作

### 2.1 StringRedisTemplate — String & Hash

[StringRedisTemplate](src/main/java/com/example/redis/_demo/test05.java) 的 key/value 全部按 **String** 序列化，适合纯字符串场景：

```java
@Autowired
private StringRedisTemplate stringRedisTemplate;

ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

ops.set("n1", "测试数据1");                                          // 写入
ops.get("n1");                                                       // 读取
ops.setIfAbsent("n4", "值", 25, TimeUnit.SECONDS);                   // NX + EX，返回是否成功
ops.increment("n2");                                                 // 原子自增
```

[Hash 操作](src/main/java/com/example/redis/_demo/test06.java)：

```java
HashOperations<String, Object, Object> hashOps = stringRedisTemplate.opsForHash();

hashOps.put("h1", "name", "刘德华");    // 单个 field
hashOps.putAll("h2", map);              // 批量写入 Map
hashOps.get("h1", "name");              // 读单个 field
hashOps.entries("h2");                  // 读全部 → Map<Object,Object>
hashOps.keys("h2") / hashOps.values("h2"); // 分别取 field 集合 / value 集合
```

### 2.2 RedisTemplate — 存储任意 Java 对象

[test07.java](src/main/java/com/example/redis/_demo/test07.java) — 核心要点：**序列化器选择**

```java
// 默认序列化器是 JdkSerializationRedisSerializer，存进去是乱码且要求对象实现 Serializable
// 生产做法：key 用 StringSerializer，value 用 JSON 序列化器

redisTemplate.setKeySerializer(new StringRedisSerializer());
redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

valueOperations.set("student1", new Student(1, "张三", "男"));  // 自动转 JSON 存储
```

### 2.3 统一配置（推荐）

[RedisConfig.java](src/main/java/com/example/redis/config/RedisConfig.java) — 将上述序列化配置抽取为全局 Bean：

```java
@Bean
public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    return template;
}
```

---

## 三、封装 Service 层

[RedisService.java](src/main/java/com/example/redis/service/RedisService.java) + [RedisController.java](src/main/java/com/example/redis/controller/RedisController.java) 提供了 REST API 封装：

| 接口 | 方法 | 说明 |
|------|------|------|
| GET `/api/redis/set?key=&value=` | set | 写入 |
| GET `/api/redis/get?key=` | get | 读取 |
| GET `/api/redis/delete?key=` | delete | 删除 |
| GET `/api/redis/setWithExpire?key=&value=&timeout=` | setWithExpire | 写入+过期时间 |

---

## 架构总结

```
┌─────────────────────────────────────────────┐
│  RedisController (REST API)                  │
│         ↓                                    │
│  RedisService (业务封装)                      │
│         ↓                                    │
│  ┌─────────────────┐  ┌──────────────────┐  │
│  │ RedisTemplate    │  │ StringRedisTem.. │  │
│  │ (自定义序列化)    │  │ (String 场景)     │  │
│  └────────┬────────┘  └────────┬─────────┘  │
│           ↓                    ↓             │
│        JedisPool ─────────→  Redis Server    │
└─────────────────────────────────────────────┘
```

**选型建议：**
- 纯字符串 / 简单 KV → `StringRedisTemplate`
- 需要存 Java 对象 → `RedisTemplate` + JSON 序列化器
- 高并发场景 → 必须走 `JedisPool` 连接池
