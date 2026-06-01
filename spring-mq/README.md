# Spring Boot + RabbitMQ 入门实战

## 一、什么是消息队列？

消息队列（Message Queue，简称 MQ）是一种**进程间通信方式**，核心思想很简单：

> 生产者把消息丢进队列，消费者从队列里取消息处理。

听起来平淡无奇，但它解决了分布式系统中几个非常实际的问题：

| 问题 | 没有 MQ | 有 MQ |
|------|---------|-------|
| 系统耦合 | 下单服务必须等库存服务返回，一个挂全挂 | 下单服务只管发消息，库存服务慢慢消费 |
| 流量突增 | 秒杀请求直接打爆数据库 | 请求先进队列排队，消费者按能力消费 |
| 异步处理 | 用户注册后同步发邮件、发短信，响应慢 | 发消息到队列，立即返回，邮件服务异步处理 |

一句话总结：**解耦、异步、削峰**。

## 二、RabbitMQ 核心概念

RabbitMQ 是目前最流行的开源消息代理之一，由 Erlang 语言编写，原生支持 AMQP 协议。

理解 RabbitMQ 的关键在于搞清楚消息的流转路径：

```
Producer → Exchange → [Binding + Routing Key] → Queue → Consumer
```

### 2.1 Exchange（交换机）

Exchange 是消息的**第一站**，生产者不会直接把消息发到队列，而是发到 Exchange。Exchange 根据规则将消息路由到一个或多个队列。

RabbitMQ 提供四种 Exchange 类型：

| 类型 | 路由规则 | 典型场景 |
|------|----------|----------|
| **Direct** | Routing Key 精确匹配 | 点对点通信，如订单处理 |
| **Fanout** | 广播到所有绑定队列，忽略 Routing Key | 广播通知，如缓存更新 |
| **Topic** | Routing Key 通配符匹配（`*` 匹配一个词，`#` 匹配零或多个词） | 按主题订阅，如日志分级收集 |
| **Headers** | 根据消息头属性匹配，性能较差 | 极少使用 |

本项目使用的是 **Direct Exchange**，最简单也最常用。

### 2.2 Queue（队列）

队列是消息的**最终存储地**，消费者从队列中取消息。关键属性：

- **durable**：是否持久化。`true` 表示 RabbitMQ 重启后队列仍在
- **exclusive**：是否排他。`true` 表示仅当前连接可用，连接断开自动删除
- **autoDelete**：是否自动删除。`true` 表示没有消费者时自动删除

### 2.3 Binding & Routing Key

Binding 是 Exchange 和 Queue 之间的**绑定关系**，Routing Key 是绑定时指定的**路由规则**。

以 Direct Exchange 为例：

```
Exchange: order.exchange
  ├── Binding(routingKey=order.create) → Queue: order.create.queue
  └── Binding(routingKey=order.cancel) → Queue: order.cancel.queue
```

生产者发送消息时指定 Routing Key = `order.create`，消息就会被路由到 `order.create.queue`。

### 2.4 整体流转图

```
┌──────────┐     ┌─────────────────┐     ┌──────────────┐     ┌──────────┐
│ Producer  │────▶│ Direct Exchange  │────▶│    Queue     │────▶│ Consumer │
│ (发消息)   │     │ (按Key路由)       │     │ (存消息)      │     │ (处理消息) │
└──────────┘     └─────────────────┘     └──────────────┘     └──────────┘
                        │                       ▲
                        └─── Binding ────────────┘
                          routingKey: mq.routing.key
```

## 三、项目实现

本项目基于 Spring Boot 3.5 + `spring-boot-starter-amqp`，实现了 Direct Exchange 模式下的消息发送与消费。

### 3.1 项目结构

```
com.hfh.api/
├── ApiApplication.java              # 启动类
├── config/
│   └── RabbitMQConfig.java          # Exchange、Queue、Binding 声明
├── consumer/
│   └── MessageConsumer.java         # 消息消费者（@RabbitListener）
├── controller/
│   └── MessageController.java       # REST 接口
└── producer/
    └── MessageProducer.java         # 消息生产者（RabbitTemplate）
```

### 3.2 配置说明

```yaml
spring:
  rabbitmq:
    host: localhost
    port: 5672          # AMQP 协议端口
    username: root
    password: xxx
    virtual-host: /     # vhost，类似数据库的"命名空间"，隔离不同环境

mq:
  exchange:
    direct: mq.exchange.direct    # Direct Exchange 名称
  queue:
    default: mq.queue.default     # 队列名称
  routing:
    key: mq.routing.key           # 路由键
```

> **小知识**：RabbitMQ 默认端口 5672 是 AMQP 协议端口，15672 是管理界面端口（需安装 `rabbitmq_management` 插件）。

### 3.3 核心代码解析

#### 声明 Exchange、Queue、Binding

```java
@Bean
public DirectExchange directExchange() {
    // 参数：名称、是否持久化、是否自动删除
    return new DirectExchange(exchangeName, true, false);
}

@Bean
public Queue defaultQueue() {
    // 参数：名称、是否持久化、是否排他、是否自动删除
    return new Queue(queueName, true, false, false);
}

@Bean
public Binding binding(Queue defaultQueue, DirectExchange directExchange) {
    // 将队列绑定到交换机，指定 routingKey
    return BindingBuilder.bind(defaultQueue).to(directExchange).with(routingKey);
}
```

> Spring AMQP 的 `@Bean` 声明方式会在应用启动时自动在 RabbitMQ 中创建这些资源，无需手动在管理界面操作。

#### 发送消息

```java
rabbitTemplate.convertAndSend(exchangeName, routingKey, message);
```

`RabbitTemplate` 是 Spring AMQP 提供的发送消息模板，`convertAndSend` 方法参数依次为：Exchange 名称、Routing Key、消息体。

#### 消费消息

```java
@RabbitListener(queues = "${mq.queue.default}")
public void receive(String message) {
    log.info("收到消息: {}", message);
}
```

`@RabbitListener` 注解标记的方法会自动监听指定队列，有消息到达时自动触发。方法参数即为消息内容。

### 3.4 运行与测试

**1. 启动 RabbitMQ**

```bash
docker run -d --name my-rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management-alpine
```

**2. 启动 Spring Boot 应用**

```bash
mvn spring-boot:run
```

**3. 发送消息**

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/message/send" -Method POST -ContentType "text/plain" -Body "Hello RabbitMQ!"
```

**4. 观察日志**

```
Producer: 发送消息到 Exchange: mq.exchange.direct, RoutingKey: mq.routing.key, 消息内容: Hello RabbitMQ!
Consumer: 收到消息: Hello RabbitMQ!
```

**5. 管理界面**

浏览器访问 `http://localhost:15672`，可以在 Exchanges 和 Queues 页签中查看交换机和队列的状态。

## 四、常见问题

### Q: Exchange 和 Queue 一定要手动声明吗？

不需要。Spring AMQP 的 `@Bean` 方式会在启动时自动声明。也可以用 `@Queue` 和 `@Exchange` 注解在 `@RabbitListener` 上直接声明。

### Q: 消息丢了怎么办？

RabbitMQ 支持消息确认机制：
- **生产者确认**：`publisher-confirm-type` 开启确认回调，确保消息成功到达 Exchange
- **消费者确认**：默认自动 ACK，可改为手动 ACK，处理失败时消息重新入队
- **消息持久化**：Exchange 和 Queue 设置 durable=true，消息设置 deliveryMode=2

### Q: Direct 和 Topic Exchange 怎么选？

- 如果路由键是固定的、一对一的，用 Direct
- 如果需要按模式匹配（如 `order.*` 匹配 `order.create` 和 `order.cancel`），用 Topic

## 五、参考

- [RabbitMQ 官方文档](https://www.rabbitmq.com/documentation.html)
- [Spring AMQP 官方文档](https://docs.spring.io/spring-amqp/reference/)
