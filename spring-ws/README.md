# 🚀 Spring Boot WebSocket 实战：从零搭建实时通信服务

> 💡 **本文将带你手把手实现一个完整的 WebSocket 服务端**，包含连接管理、消息交互和定时心跳推送等核心功能。适合正在学习 WebSocket 或者需要在项目中集成实时通信功能的开发者阅读。

---

## ✨ 项目简介

本项目是一个基于 **Spring Boot 3.3.4 + Jakarta WebSocket API** 的轻量级 WebSocket 服务端示例，展示了如何在 Spring Boot 中快速构建实时双向通信能力。

### 🎯 核心特性

- ✅ **WebSocket 连接管理**：使用线程安全的 `ConcurrentHashMap` 管理所有在线会话
- ✅ **双向消息通信**：支持接收客户端消息并返回响应
- ✅ **定时心跳机制**：每 2 秒自动向所有连接的客户端推送心跳消息
- ✅ **优雅的生命周期钩子**：完善的连接建立/关闭事件处理
- ✅ **生产级代码规范**：采用 Lombok + SLF4J 日志体系

---

## 🛠️ 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| **Spring Boot** | 3.3.4 | 基础框架 |
| **Java** | 17 | 运行环境 |
| **Jakarta WebSocket** | - | WebSocket API（替代旧的 javax.websocket） |
| **Lombok** | latest | 简化代码 |
| **Maven** | - | 构建工具 |

---

## 📁 项目结构

```
spring-ws/
├── src/main/java/com/example/demo/
│   ├── config/
│   │   └── WebSocketConfig.java      # WebSocket 配置类
│   ├── serve/
│   │   └── WsServerEndPoint.java     # WebSocket 端点实现（核心）
│   └── DemoApplication.java          # 启动类
├── src/main/resources/
│   └── application.yml               # 应用配置
├── pom.xml                           # Maven 依赖配置
└── App.vue                           # 前端测试页面（可选）
```

---

## 🔧 核心代码解析

### 1️⃣ WebSocket 配置类

```java
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
```

**💡 关键点**：
- `ServerEndpointExporter` 是 Spring Boot 集成 Jakarta WebSocket 的关键组件
- 它会自动扫描带有 `@ServerEndpoint` 注解的类并注册为 WebSocket 端点
- **注意**：这个 Bean 只在独立运行模式下需要，如果部署到外部 Servlet 容器则不需要

---

### 2️⃣ WebSocket 端点实现（核心）

#### 📍 端点定义与状态管理

```java
@ServerEndpoint("/myWs")  // 定义 WebSocket 访问路径
@Component                 // 交给 Spring 管理
@Slf4j                    // 自动生成日志对象
public class WsServerEndPoint {
    // 使用线程安全的 ConcurrentHashMap 存储所有在线会话
    static Map<String, Session> sessionMap = new ConcurrentHashMap<>();
}
```

**🎓 为什么用 ConcurrentHashMap？**
- 普通的 `HashMap` 在多线程环境下不安全，可能导致数据不一致
- `ConcurrentHashMap` 采用分段锁机制，保证高并发下的线程安全性和性能
- WebSocket 场景下多个客户端同时连接/断开是常态，必须保证线程安全

#### 🔄 生命周期回调方法

**① 连接建立时（@OnOpen）**
```java
@OnOpen
public void onOpen(Session session) {
    sessionMap.put(session.getId(), session);
    log.info("websocket is open");
}
```
- 当客户端成功建立 WebSocket 连接时触发
- 将新会话保存到 `sessionMap` 中，用于后续消息推送

**② 接收消息时（@OnMessage）**
```java
@OnMessage
public String onMessage(String text) {
    log.info("接受到一条消息：" + text);
    return "已经接收到消息";
}
```
- 接收客户端发送的消息并记录日志
- 返回响应字符串给客户端（可选）

**③ 连接关闭时（@OnClose）**
```java
@OnClose
public void onClose(Session session) {
    sessionMap.remove(session.getId());
    log.info("websocket is close");
}
```
- 客户端断开连接时清理会话资源
- **重要**：避免内存泄漏！

#### ⏱️ 定时心跳推送

```java
@Scheduled(fixedRate = 2000)  // 每 2 秒执行一次
public void sendMessage() throws IOException {
    Set<String> keySet = sessionMap.keySet();
    for (String sessionId : keySet) {
        Session session = sessionMap.get(sessionId);
        RemoteEndpoint.Basic basicRemote = session.getBasicRemote();
        basicRemote.sendText("心跳");
    }
}
```

**💡 心跳的作用**：
- **检测连接活性**：确认客户端是否仍然在线
- **防止连接超时**：某些代理服务器/负载均衡器会关闭长时间无活动的连接
- **保持 NAT 映射**：在客户端位于 NAT 后面时保持端口映射有效

**⚠️ 生产环境优化建议**：
- 可以在心跳中携带时间戳或序列号
- 客户端收到心跳后应回复 PONG 消息
- 可以统计连续未响应次数，自动关闭"僵尸"连接

---

### 3️⃣ 启动类配置

```java
@EnableScheduling  // 开启定时任务支持
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

**🔑 关键注解**：
- `@EnableScheduling`：启用 Spring 的定时任务调度功能，否则 `@Scheduled` 注解不会生效

---

## 🚀 快速开始

### 前置要求

- JDK 17+
- Maven 3.6+

### 运行步骤

1. **克隆项目**
```bash
cd spring-ws
```

2. **启动服务**
```bash
mvn spring-boot:run
```

3. **验证启动**

服务默认运行在 `http://localhost:8080`

WebSocket 端点地址：`ws://localhost:8080/myWs`

### 测试 WebSocket 连接

你可以使用以下方式测试：

#### 方式一：浏览器控制台（推荐）

打开浏览器开发者工具（F12），在 Console 中输入：

```javascript
const ws = new WebSocket('ws://localhost:8080/myWs');

ws.onopen = () => console.log('✅ 连接已建立');

ws.onmessage = (event) => console.log('📨 收到消息:', event.data);

ws.onclose = () => console.log('❌ 连接已关闭');

// 发送测试消息
ws.send('Hello WebSocket!');
```

**预期输出**：
```
✅ 连接已建立
📨 收到消息: 心跳
📨 收到消息: 心跳
... (每 2 秒一次)
```

#### 方式二：使用 wscat 工具

```bash
npm install -g wscat
wscat -c ws://localhost:8080/myWs
```

#### 方式三：Postman / Apifox

新建 WebSocket 请求，地址填入 `ws://localhost:8080/myWs` 即可测试

---

## 📊 架构流程图

```
┌─────────────┐     1. HTTP Upgrade      ┌──────────────────┐
│             │ ──────────────────────▶  │                  │
│   Client    │                          │  Spring Boot App │
│ (Browser/   │ ◀──────────────────────  │                  │
│  App)       │     2. Connection OK     │  ┌────────────┐  │
│             │                          │  │WsServerEnd │  │
│             │     3. Send Message      │  │   Point     │  │
│             │ ──────────────────────▶  │  └────────────┘  │
│             │                          │         │        │
│             │ ◀─ 4. Response/Heartbeat │         ▼        │
│             │     (every 2 seconds)    │  ┌────────────┐  │
│             │                          │  │Session Map │  │
│             │     5. Close Connection  │  │(Concurrent)│  │
│             │ ──────────────────────▶  │  └────────────┘  │
└─────────────┘                          └──────────────────┘
```

---

## 🎨 应用场景扩展

本项目可以作为以下场景的基础模板：

- **📊 实时数据大屏**：股票行情、系统监控指标推送
- **💬 在线聊天室**：即时通讯、客服系统
- **🎮 多人协作**：在线文档编辑、白板协作
- **📈 实时通知**：订单状态更新、消息提醒
- **🤖 IoT 设备监控**：传感器数据实时上报

---

## ⚠️ 注意事项与最佳实践

### 安全性

1. **添加身份认证**：在 `@OnOpen` 时验证 Token 或 Session
2. **使用 WSS**：生产环境务必启用 TLS 加密（`wss://`）
3. **限制连接数**：防止恶意客户端耗尽服务器资源
4. **输入校验**：对客户端发送的消息进行过滤和校验

### 性能优化

1. **消息批量发送**：高频场景下考虑合并消息
2. **连接池管理**：合理设置最大连接数和超时时间
3. **集群部署**：使用 Redis Pub/Sub 或消息队列实现跨节点通信
4. **监控告警**：统计在线人数、消息量等指标

### 异常处理

建议在生产环境中添加：

```java
@OnError
public void onError(Session session, Throwable error) {
    log.error("WebSocket错误", error);
    sessionMap.remove(session.getId());
}
```

---

## 📝 总结

通过这个项目，我们学习了：

✅ Spring Boot 3 集成 Jakarta WebSocket 的标准姿势  
✅ WebSocket 生命周期管理和会话存储  
✅ 定时任务实现心跳机制的实战技巧  
✅ 并发安全的数据结构选择（ConcurrentHashMap）  
✅ 生产环境的注意事项和优化方向  

> **WebSocket 是现代 Web 应用实时通信的核心技术之一**，掌握它将大大拓展你的技术边界！🎉

---

## 📚 参考资源

- [Jakarta WebSocket 规范](https://jakarta.ee/specifications/websocket/)
- [Spring Boot 官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [MDN WebSocket API](https://developer.mozilla.org/zh-CN/docs/Web/API/WebSocket)

---

*本文项目源码可在当前仓库中查看，欢迎 Star 和 Fork！⭐*
