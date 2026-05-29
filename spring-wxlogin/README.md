# Spring 微信公众号扫码登录实战

> 基于 Spring Boot 3.x + WxJava 实现的微信公众号扫码登录完整解决方案

## 📖 项目简介

在日常开发中，微信扫码登录已经成为移动端应用的标准配置之一。本项目提供了一个开箱即用的 **Spring Boot 微信公众号扫码登录** 实现方案，采用二维码 + Redis 轮询的模式，实现了从扫码到登录的完整流程。

### ✨ 核心特性

- 🔐 **安全可靠**：完整的微信签名验证机制
- ⚡ **高性能**：基于 Redis 缓存 + 连接池优化
- 🎯 **易于集成**：标准 RESTful API 设计
- 📱 **响应式前端**：内置测试页面，支持实时状态展示
- 🔄 **自动过期**：二维码和会话自动过期机制

## 🏗️ 技术架构

```
┌─────────────────────────────────────────────────────┐
│                    前端 (Browser)                     │
│   ┌──────────┐    ┌─────────────────┐               │
│   │ 二维码展示 │◄──│ 轮询登录状态     │               │
│   └────┬─────┘    └────────┬────────┘               │
└────────┼────────────────────┼───────────────────────┘
         │                    │
         ▼                    ▼
┌─────────────────────────────────────────────────────┐
│              后端 (Spring Boot)                      │
│                                                      │
│  ┌──────────────┐   ┌──────────────────────────┐   │
│  │ WeChatController│  │ WeChatSignController     │   │
│  │ - 生成二维码    │   │ - 签名验证               │   │
│  │ - 查询登录状态  │   │ - 处理扫码回调            │   │
│  └──────┬───────┘   └──────────┬───────────────┘   │
│         │                      │                     │
│         ▼                      ▼                     │
│  ┌──────────────────────────────────────────┐       │
│  │              Redis (Jedis)                │       │
│  │   sceneId → OpenId 映射关系存储            │       │
│  └──────────────────────────────────────────┘       │
│                                                      │
│  ┌──────────────────────────────────────────┐       │
│  │           WxJava SDK                      │       │
│  │      微信公众号 API 封装                   │       │
│  └──────────────────────────────────────────┘       │
└─────────────────────────────────────────────────────┘
         │
         ▼
┌─────────────────────────────────────────────────────┐
│              微信公众平台                             │
│        二维码生成 / 消息推送 / 签名验证                 │
└─────────────────────────────────────────────────────┘
```

## 🛠️ 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | LTS 版本，支持最新特性 |
| Spring Boot | 3.4.4 | 最新稳定版 |
| WxJava (weixin-java-open) | 3.8.0 | 微信开发工具包 |
| Jedis | 4.3.1 | Redis Java 客户端 |
| Commons Codec | 1.15 | SHA1 签名加密 |

## 🚀 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- Redis 6.0+
- 微信公众号（已认证的服务号）

### 1️⃣ 配置微信参数

编辑 `src/main/resources/application.yml`：

```yaml
wx:
  appid: your_appid          # 微信公众号 AppID
  appsecret: your_secret     # 微信公众号 AppSecret
```

> 💡 **提示**：在 [微信公众平台](https://mp.weixin.qq.com) → 设置与开发 → 基本配置 中获取

### 2️⃣ 启动 Redis 服务

确保 Redis 服务运行在默认端口 `6379`：

```bash
redis-server
```

### 3️⃣ 运行项目

```bash
# 方式一：使用 Maven
mvn spring-boot:run

# 方式二：直接运行主类
# SpringWxloginApplication.java
```

访问 `http://localhost:8080` 即可看到测试页面。

## 📡 核心流程详解

### 登录流程时序图

```
用户            浏览器            后端服务           微信平台           Redis
 │               │                  │                 │               │
 │  打开页面      │                  │                 │               │
 │──────────────►│                  │                 │               │
 │               │                  │                 │               │
 │               │ GET /wechat/     │                 │               │
 │               │ getLoginQrCode   │                 │               │
 │               │─────────────────►│                 │               │
 │               │                  │  创建临时二维码   │               │
 │               │                  │────────────────►│               │
 │               │                  │ ◄───────────────│               │
 │               │ ◄────────────────│  返回二维码URL   │               │
 │               │                  │                 │               │
 │  显示二维码    │                  │                 │               │
 │◄──────────────│                  │                 │               │
 │               │                  │                 │               │
 │  扫描二维码    │                  │                 │               │
 │────────────────────────────────────────────────────►│               │
 │               │                  │                 │  推送扫码事件   │
 │               │                  │ ◄───────────────│               │
 │               │                  │                 │               │
 │               │                  │ 存储 sceneId     │               │
 │               │                  │ ───────────────►│               │
 │               │                  │   → openid      │               │
 │               │                  │                 │               │
 │               │ 轮询 loginStatus │                 │               │
 │               │ ────────────────►│                 │               │
 │               │                  │ 查询 Redis       │               │
 │               │                  │ ◄───────────────│               │
 │               │ ◄────────────────│  返回 openid     │               │
 │               │                  │                 │               │
 │  登录成功！    │                  │                 │               │
 │◄──────────────│                  │                 │               │
```

### 关键代码解析

#### 1. 生成登录二维码

[WeChatController.java](src/main/java/com/example/wxlogin/controller/WeChatController.java) 核心逻辑：

```java
@GetMapping("/getLoginQrCode")
public RespResult getLoginQrCode() throws WxErrorException {
    // 使用雪花算法生成唯一场景ID（用于关联用户）
    String sceneStr = IdUtil.getSnowflake().nextIdStr();

    // 创建临时二维码（有效期 3600 秒 = 1小时）
    WxMpQrCodeTicket ticket = wxMpService.getQrcodeService()
        .qrCodeCreateTmpTicket(sceneStr, 3600);

    // 获取二维码图片 URL
    String qrCodeUrl = wxMpService.getQrcodeService()
        .qrCodePictureUrl(ticket.getTicket());

    return RespResult.success(qrCodeUrl, sceneStr);
}
```

**关键点说明**：
- `sceneStr`：使用雪花算法生成的唯一标识符，作为二维码的场景值
- 临时二维码有效期为 1 小时，适合登录场景
- 同时返回二维码 URL 和 sceneId，前端需要保存 sceneId 用于后续轮询

#### 2. 处理微信回调

[WeChatSignController.java](src/main/java/com/example/wxlogin/controller/WeChatSignController.java) 签名验证与事件处理：

```java
@RequestMapping("/wx")
public void sign(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
    String method = request.getMethod();

    if ("GET".equals(method)) {
        // 首次接入验证：验证服务器签名的有效性
        // 微信会发送 signature, timestamp, nonce, echostr 参数
        if (validateSignature(signature, timestamp, nonce, TOKEN)) {
            response.getWriter().print(echostr);  // 验证通过，原样返回 echostr
        }
    } else {
        // POST 请求：处理微信推送的事件消息（如扫码关注事件）
        WxMpXmlMessage msg = WxMpXmlMessage.fromXml(request.getInputStream());
        String fromUserName = msg.getFromUser();  // 用户的 openid
        String event = msg.getEvent();             // 事件类型

        if ("subscribe".equals(event)) {
            // 用户首次扫码关注公众号
            String sceneStr = msg.getEventKey().replace("qrscene_", "");
            JedisUtil.setStr(sceneStr, fromUserName, 3600);  // 存储映射关系
        } else if ("SCAN".equals(event)) {
            // 已关注用户再次扫码
            String sceneStr = msg.getEventKey();
            JedisUtil.setStr(sceneStr, fromUserName, 3600);
        }
    }
}
```

**关键点说明**：
- **GET 请求**：微信服务器首次验证时会调用，需返回 `echostr`
- **POST 请求**：用户扫码后微信会推送 XML 格式的消息
- **subscribe 事件**：新用户关注时触发，`EventKey` 格式为 `qrscene_xxx`
- **SCAN 事件**：老用户扫码时触发，`EventKey` 就是场景值

#### 3. 签名验证算法

```java
public static boolean validateSignature(String signature,
                                        String timestamp,
                                        String nonce,
                                        String token) {
    // 1. 将 token、timestamp、nonce 三个参数进行字典序排序
    String[] arr = new String[]{token, timestamp, nonce};
    Arrays.sort(arr);

    // 2. 将三个参数字符串拼接成一个字符串进行 sha1 加密
    StringBuilder sb = new StringBuilder();
    for (String s : arr) {
        sb.append(s);
    }

    // 3. 将加密后的字符串与 signature 对比，判断是否来自微信
    String sha1 = DigestUtils.sha1Hex(sb.toString());
    return sha1.equals(signature);
}
```

**为什么需要签名验证？**
- 防止恶意请求伪造微信回调
- 确保数据来源的可信性
- 微信官方要求的安全机制

#### 4. 查询登录状态

```java
@GetMapping("/loginStatus")
public RespResult loginStatus(@RequestParam String sceneId) {
    // 根据 sceneId 从 Redis 查询是否已存储用户的 openid
    String openid = JedisUtil.getStr(sceneId);
    if (openid != null) {
        return RespResult.success("success", openid);  // 用户已扫码
    } else {
        return RespResult.fail("fail");                 // 等待扫码中...
    }
}
```

**Redis 数据结构设计**：

```
Key:   sceneId (雪花算法生成的唯一ID)
Value: openid (微信用户的唯一标识)
TTL:   3600 秒 (1小时自动过期)
```

## 🎨 前端实现

[index.html](src/main/resources/static/index.html) 提供了完整的测试页面：

### 核心功能

1. **获取二维码**：页面加载时自动调用后端 API 获取登录二维码
2. **轮询状态**：每 3 秒查询一次登录状态
3. **状态展示**：实时显示当前登录进度

### 关键代码片段

```javascript
// 1. 获取登录二维码
fetch('/wechat/getLoginQrCode')
    .then(response => response.json())
    .then(data => {
        document.getElementById('qrcode').src = data.message;  // 显示二维码
        localStorage.setItem('sceneId', data.data);            // 保存 sceneId
    });

// 2. 轮询查询登录状态（每 3 秒一次）
const checkLoginStatus = () => {
    const sceneId = localStorage.getItem('sceneId');
    fetch(`/wechat/loginStatus?sceneId=${sceneId}`)
        .then(response => response.json())
        .then(data => {
            if (data.message === 'success') {
                document.getElementById('status').innerText = '登录成功！';
                // TODO: 跳转到首页或执行其他操作
            }
        });
};

setInterval(checkLoginStatus, 3000);
```

## 📁 项目结构

```
spring-wxlogin/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/wxlogin/
│   │   │       ├── config/
│   │   │       │   ├── CustomCorsConfig.java      # CORS 跨域配置
│   │   │       │   ├── GlobalCorsConfig.java       # 全局跨域配置
│   │   │       │   └── WeChatConfig.java           # 微信 API 配置
│   │   │       ├── controller/
│   │   │       │   ├── WeChatController.java       # 登录接口控制器
│   │   │       │   └── WeChatSignController.java   # 微信回调控制器
│   │   │       ├── utils/
│   │   │       │   ├── JedisUtil.java              # Redis 工具类
│   │   │       │   └── RespResult.java             # 统一响应结果
│   │   │       └── SpringWxloginApplication.java   # 启动类
│   │   └── resources/
│   │       ├── application.yml                     # 应用配置
│   │       └── static/
│   │           └── index.html                       # 测试页面
│   └── test/
│       └── java/                                    # 单元测试
├── pom.xml                                          # Maven 配置
└── README.md                                        # 项目文档
```

## 🔧 API 接口文档

### 1. 获取登录二维码

**接口地址**: `GET /wechat/getLoginQrCode`

**响应示例**:

```json
{
  "code": 200,
  "message": "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=xxx",
  "data": "1234567890123456789"
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | int | 状态码，200 表示成功 |
| message | string | 二维码图片 URL |
| data | string | 场景 ID（用于后续轮询） |

---

### 2. 查询登录状态

**接口地址**: `GET /wechat/loginStatus?sceneId={sceneId}`

**参数说明**:

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| sceneId | string | 是 | 获取二维码时返回的场景 ID |

**成功响应示例** (用户已扫码):

```json
{
  "code": 200,
  "message": "success",
  "data": "oXXXXX_openid_string"
}
```

**等待响应示例** (用户未扫码):

```json
{
  "code": 500,
  "message": "fail"
}
```

---

### 3. 微信回调接口

**接口地址**: `GET|POST /sign/wx`

**GET 参数** (服务器验证):

| 参数 | 类型 | 说明 |
|------|------|------|
| signature | string | 微信加密签名 |
| timestamp | string | 时间戳 |
| nonce | string | 随机数 |
| echostr | string | 随机字符串（验证时返回） |

**POST Body** (XML 格式):

```xml
<xml>
  <ToUserName><![CDATA[toUser]]></ToUserName>
  <FromUserName><![CDATA[fromUser]]></FromUserName>
  <CreateTime>1348831860</CreateTime>
  <MsgType><![CDATA[event]]></MsgType>
  <Event><![CDATA[subscribe]]></Event>
  <EventKey><![CDATA[qrscene_123456789]]></EventKey>
</xml>
```

## 🎯 应用场景

本项目适用于以下场景：

✅ **Web 应用登录**：PC 端网站使用微信扫码登录  
✅ **后台管理系统**：管理员使用微信扫码快速登录  
✅ **第三方应用授权**：OAuth 授权登录替代方案  
✅ **线下活动签到**：会议/活动扫码签到系统  

## ⚠️ 注意事项

### 1. 公众号类型要求

- ✅ **已认证的服务号**：支持所有接口
- ❌ **订阅号**：不支持生成带参数二维码
- ❌ **未认证的号**：部分接口受限

### 2. 安全建议

⚠️ **生产环境注意事项**：

1. **AppSecret 安全**
   - 不要将 AppSecret 提交到代码仓库
   - 使用环境变量或配置中心管理
   - 定期更换 AppSecret

2. **HTTPS 要求**
   - 生产环境必须使用 HTTPS
   - 回调域名需要在微信公众平台配置

3. **Token 保护**
   - Token 应该是随机且复杂的字符串
   - 不要使用示例中的 `xiaohelikesleep`

4. **Redis 安全**
   - 设置 Redis 访问密码
   - 不要暴露在公网
   - 定期备份数据

### 3. 性能优化建议

- **轮询间隔**：根据业务需求调整轮询频率（建议 2-5 秒）
- **Redis 过期时间**：根据实际需求调整 TTL（当前为 1 小时）
- **连接池配置**：根据并发量调整 JedisPool 大小
- **缓存策略**：可以考虑增加本地缓存减少 Redis 访问

## 🐛 常见问题

### Q1: 二维码无法显示？

**可能原因**：
- AppID 或 AppSecret 配置错误
- 公众号类型不正确（需要服务号）
- 网络无法访问微信 API

**排查步骤**：
1. 检查 `application.yml` 中的配置
2. 确认公众号已认证且为服务号
3. 查看后端日志是否有异常信息

### Q2: 扫码后没有反应？

**可能原因**：
- 回调 URL 未正确配置
- Redis 未启动或连接失败
- 签名验证失败

**排查步骤**：
1. 在微信公众平台检查服务器配置
2. 确认 Redis 服务正常运行
3. 查看 `/sign/wx` 接口的日志输出

### Q3: 如何部署到生产环境？

**推荐步骤**：
1. 修改配置文件，使用环境变量管理敏感信息
2. 配置 HTTPS 证书
3. 在微信公众平台设置正确的回调域名
4. 使用 Nginx 反向代理
5. 配置防火墙规则，只开放必要端口

## 📊 扩展方向

本项目为基础版本，可以根据需要进行以下扩展：

- [ ] **用户系统集成**：将 openid 与业务系统的用户表关联
- [ ] **JWT Token**：登录成功后生成 JWT 用于后续接口鉴权
- [ ] **多终端同步**：支持 PC/移动端同时登录
- [ ] **登录日志**：记录登录时间、IP、设备等信息
- [ ] **限流防护**：防止暴力破解和恶意攻击
- [ ] **WebSocket 升级**：用 WebSocket 替代轮询，提升性能

## 📝 更新日志

### v1.0.0 (2026-05-29)

✨ 初始版本发布：
- 实现微信公众号扫码登录核心功能
- 包含完整的签名验证机制
- 提供 Redis 缓存支持
- 内置前端测试页面

## 📄 许可证

MIT License

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request！

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

---

## 💬 交流反馈

如果你在使用过程中遇到任何问题，欢迎提 Issue 进行讨论！

> **最后更新**: 2026-05-29 | **作者**: Spring 开发团队
