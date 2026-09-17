# Spring Email - 企业级邮件发送方案

基于 Spring Boot 3.5 + JavaMailSender + Thymeleaf 模板的异步邮件服务，支持简单文本/HTML/模板邮件、群发、附件、批量发送。

## 技术选型

| 依赖 | 版本 | 用途 |
|------|------|------|
| spring-boot-starter-mail | 3.5.14 | 邮件发送核心 |
| spring-boot-starter-thymeleaf | 3.5.14 | HTML 邮件模板渲染 |
| knife4j-openapi3-jakarta | 4.5.0 | API 文档（Swagger 增强） |
| spring-boot-starter-validation | 3.5.14 | 参数校验（自定义注解） |
| lombok | - | 减少样板代码 |

## 核心功能

### 1. 四种发送模式

```
POST /api/email/simple    → 简单文本邮件（支持群发+抄送）
POST /api/email/html      → HTML 富文本邮件（群发+抄送+密送+附件）
POST /api/email/template  → Thymeleaf 模板邮件（动态变量替换）
POST /api/email/batch     → 批量异步群发（每个收件人独立任务）
```

### 2. 异步发送机制

所有邮件通过 `@Async("mailTaskExecutor")` 异步执行，不阻塞 HTTP 线程：

```java
@Async("mailTaskExecutor")
public CompletableFuture<Void> sendSimpleEmailAsync(EmailRequest request) {
    // 返回 CompletableFuture，调用方可选择等待或 fire-and-forget
}
```

线程池配置见 [AsyncConfig.java](src/main/java/com/example/hfh/config/AsyncConfig.java)。

### 3. 收件人校验（自定义 Validation 注解）

```java
@Data
@AtLeastOneRecipient(message = "收件人或收件人列表至少填写一项")
public class EmailRequest implements Recipient {
    @Email private String to;                    // 单收件人
    @ValidEmailList private List<String> toList;  // 多收件人（群发）
    @ValidEmailList private List<String> cc;      // 抄送
    @ValidEmailList private List<String> bcc;     // 密送
}
```

- `@AtLeastOneRecipient`：`to` 和 `toList` 至少填一个
- `@ValidEmailList`：列表中每个邮箱格式校验

详见 [validation 包](src/main/java/com/example/hfh/validation/)。

## 快速开始

### 1. 修改邮箱配置

编辑 [application.yaml](src/main/resources/application.yaml)：

```yaml
spring:
  mail:
    host: smtp.163.com          # SMTP 服务器
    port: 994                   # SSL端口：163/QQ用465或994
    username: your@email.com    # 发件邮箱
    password: XXXXXXXX          # 授权码（非登录密码）
    protocol: smtps
    properties:
      mail:
        smtp:
          ssl:
            enable: true        # 启用SSL
```

**常用邮箱 SMTP 配置：**

| 邮箱 | Host | SSL端口 | 非SSL端口 |
|------|------|---------|----------|
| 163 | smtp.163.com | 465/994 | 25 |
| QQ | smtp.qq.com | 465 | 587 |
| Gmail | smtp.gmail.com | 465 | 587 |

### 2. 获取授权码

以 163 邮箱为例：
1. 登录 → 设置 → POP3/SMTP/IMAP
2. 开启 SMTP 服务
3. 生成授权码（需手机验证）

### 3. 启动项目

```bash
./mvnw spring-boot:run
```

访问 http://localhost:8080/doc.html 查看 Knife4j API 文档。

## API 使用示例

### 发送简单文本邮件

```bash
curl -X POST http://localhost:8080/api/email/simple \
  -H "Content-Type: application/json" \
  -d '{
    "to": "recipient@example.com",
    "subject": "测试邮件",
    "content": "这是一封测试邮件"
  }'
```

### 群发 + 抄送 + 密送

```bash
curl -X POST http://localhost:8080/api/email/html \
  -H "Content-Type: application/json" \
  -d '{
    "toList": ["a@example.com", "b@example.com"],
    "cc": ["cc@example.com"],
    "bcc": ["secret@example.com"],
    "subject": "会议通知",
    "content": "<h2>明天下午3点开会</h2>",
    "attachments": ["/path/to/file.pdf"]
  }'
```

### 发送模板邮件

```bash
curl -X POST http://localhost:8080/api/email/template \
  -H "Content-Type: application/json" \
  -d '{
    "to": "user@example.com",
    "subject": "欢迎加入",
    "TemplateName": "mail/welcome",
    "variables": {
      "username": "张三",
      "code": "886622",
      "link": "https://example.com/activate",
      "expireMinutes": 30
    }
  }'
```

模板文件位置：[welcome.html](src/main/resources/templates/mail/welcome.html)

### 批量发送

```bash
curl -X POST http://localhost:8080/api/email/batch \
  -H "Content-Type: application/json" \
  -d '[
    {"to": "a@example.com", "subject": "邮件1", "content": "内容1"},
    {"to": "b@example.com", "subject": "邮件2", "content": "内容2"}
  ]'
```

## 项目结构

```
src/main/java/com/hfh/
├── config/
│   ├── MailConfig.java           # JavaMailSender Bean配置
│   └── AsyncConfig.java          # 异步线程池配置
├── controller/
│   └── EmailController.java      # REST API（4个端点）
├── service/
│   └── EmailService.java         # 核心业务逻辑（4种发送方式）
├── dto/
│   ├── EmailRequest.java         # 简单/HTML邮件请求
│   ├── TemplateEmailRequest.java # 模板邮件请求
│   ├── WelcomeTemplateVariables.java # 模板变量DTO
│   └── Recipient.java            # 收件人接口
├── validation/
│   ├── AtLeastOneRecipient.java  # 自定义校验注解
│   └── ValidEmailList.java       # 邮箱列表校验注解
└── exception/
    └── GlobalExceptionHandler.java # 全局异常处理
```

## 关键设计决策

1. **为什么用 `CompletableFuture<Void>`？**
   - 调用方可以选择 `.thenAccept()` 处理成功/失败回调，也可以直接忽略（fire-and-forget）
   - Controller 层立即返回 200，实际发送在后台进行

2. **为什么群发用 `toList` 而不是循环调用？**
   - 单次 SMTP 连接发送多个收件人，性能更高
   - 所有收件人互相可见（适合通知类邮件）
   - 如需隐藏其他收件人，改用批量接口 `/batch`

3. **模板引擎为什么选 Thymeleaf？**
   - Spring Boot 官方推荐，天然集成
   - 支持条件判断、循环、变量转义等特性
   - 可直接输出 HTML 邮件内容

## 常见问题

**Q: 发送失败怎么排查？**
A: 开启 debug 模式（`mail.debug: true`），查看控制台 SMTP 协议日志。

**Q: 附件大小有限制吗？**
A: 默认受 SMTP 服务器限制（通常 10-25MB）。大文件建议用 OSS 链接代替。

**Q: 如何避免被标记为垃圾邮件？**
A: 
- 使用企业域名邮箱（非免费邮箱）
- 配置 SPF/DKIM/DMARC 记录
- 控制发送频率，避免短时间大量群发
