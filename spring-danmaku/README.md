# Spring Danmaku — 基于 Spring Boot + WebSocket 的实时弹幕系统

## 技术栈

| 组件 | 选型 | 版本 | 干货 |
|------|------|------|------|
| 框架 | Spring Boot | 3.5.0 | Java 17，Jakarta 命名空间 |
| 实时通信 | spring-boot-starter-websocket | — | STOMP over SockJS，`/app` 前缀发消息，`/topic` 前缀订阅广播 |
| ORM | MyBatis-Plus | 3.5.5 | `BaseMapper` 免写 CRUD，`QueryWrapper` 条件构造，分页 `IPage` |
| 数据库 | H2 (内存) | — | 零配置启动，`schema.sql` 自动建表灌数据，`/h2-console` 可视化 |
| API 文档 | Knife4j (OpenAPI 3) | 4.5.0 | 访问 `/doc.html` 即可调试接口 |
| 简化 | Lombok | — | `@Data` 生成 getter/setter/toString |

## 架构一图流

```
浏览器 (SockJS/STOMP)
  │  connect → /ws-danmaku
  │  subscribe → /topic/video/{videoId}
  │  send → /app/danmaku/send
  ▼
WebSocketConfig (@EnableWebSocketMessageBroker)
  │  消息代理 /topic  |  应用前缀 /app
  ▼
DanmakuController
  │  @MessageMapping("/danmaku/send")  ← WebSocket 入站
  │  @GetMapping  /api/danmaku/video/{id}  ← REST 查询
  ▼
DanmakuService
  │  1. 敏感词过滤  2. 持久化  3. SimpMessagingTemplate.convertAndSend 广播
  ▼
DanmakuMapper (MyBatis-Plus) → H2
```

## 核心流程

**发弹幕**：前端 STOMP.send → `@MessageMapping` 接收 → 过滤 → 入库 → `SimpMessagingTemplate` 推送 `/topic/video/{id}` → 所有订阅者收到

**看弹幕**：页面加载 → REST 拉历史弹幕 → `timeupdate` 事件按时间点回放 → WebSocket 实时接收新弹幕

## API 速查

| 方法 | 路径 | 说明 |
|------|------|------|
| WS SEND | `/app/danmaku/send` | 发送弹幕（STOMP） |
| WS SUB | `/topic/video/{videoId}` | 订阅弹幕广播 |
| GET | `/api/danmaku/video/{videoId}` | 获取视频全部弹幕 |
| GET | `/api/danmaku/video/{videoId}/timerange?start=&end=` | 按时间范围查询 |
| GET | `/api/danmaku/video/{videoId}/paged?page=&size=` | 分页查询 |

## 快速启动

```bash
mvn spring-boot:run
```

- 弹幕页面：`http://localhost:8080/index.html`
- H2 控制台：`http://localhost:8080/h2-console`（JDBC URL: `jdbc:h2:mem:danmakudb`，用户名/密码: sa/sa）
- API 文档：`http://localhost:8080/doc.html`

## 关键配置

```properties
# WebSocket 消息限制（WebSocketMessageConfig）
messageSizeLimit = 128KB        # 单条消息上限
sendBufferSizeLimit = 512KB     # 发送缓冲区
sendTimeLimit = 15s             # 发送超时

# 前端弹幕上限
MAX_DANMAKU_COUNT = 100         # 超出则移除最早弹幕 DOM
```

## 项目结构

```
src/main/java/com/example/danmaku/
├── config/
│   ├── WebSocketConfig.java          # STOMP 端点 + 消息代理配置
│   └── WebSocketMessageConfig.java   # 传输限制（消息大小/缓冲区/超时）
├── controller/
│   └── DanmakuController.java        # REST + @MessageMapping
├── dto/
│   └── DanmakuDTO.java              # 前端入参
├── mapper/
│   └── DanmakuMapper.java           # BaseMapper + 自定义 @Select
├── model/
│   └── Danmaku.java                 # @TableName 实体
└── service/
    ├── DanmakuService.java           # 核心业务：过滤→存储→广播
    └── ContentFilterService.java     # 敏感词过滤（可扩展为外部词库）
```