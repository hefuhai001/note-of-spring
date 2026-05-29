# Spring Boot 邮箱验证码登录

## 技术栈与核心依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| spring-boot-starter-mail | 3.3.3 | 邮件发送核心 |
| spring-boot-starter-thymeleaf | 3.3.3 | HTML 邮件模板渲染 |
| spring-boot-starter-data-redis | 3.3.3 | 验证码缓存与过期控制 |
| hutool-all | 5.8.19 | 工具集（本项目可用于简化随机数生成等） |

## 第三方库实战解析

### 1. Spring Mail - 邮件发送

**核心类**: `JavaMailSender`、`MimeMessageHelper`

```java
// 创建 MIME 邮件消息
MimeMessage mimeMessage = mailSender.createMimeMessage();
MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

// 关键配置项
helper.setFrom(nickname + '<' + sender + '>');  // 发件人（支持昵称+邮箱格式）
helper.setTo(email);                              // 收件人
helper.setSubject("测试发送Thymeleaf模板Email");   // 主题
helper.setText(htmlContent, true);                // true = HTML 格式
mailSender.send(mimeMessage);
```

**SMTP 配置要点** (application.yml):
```yaml
spring:
  mail:
    host: smtp.163.com          # SMTP 服务器地址
    username: xxx@163.com       # 发件邮箱
    password: 授权码            # ⚠️ 不是密码，是邮箱授权码
    properties:
      mail:
        smtp:
          socketFactoryClass: javax.net.ssl.SSLSocketFactory  # SSL 加密
```

> **各邮箱 SMTP 配置速查**:
> - 163: `smtp.163.com` / 端口 465/994
> - QQ: `smtp.qq.com` / 端口 465/587
> - 126: `smtp.126.com` / 端口 465/994

### 2. Thymeleaf - 邮件模板引擎

**优势**: 服务端渲染 HTML 邮件，支持动态变量注入

```java
Context context = new Context();
context.setVariable("text", verificationCodeList);  // 传入验证码数据
String htmlContent = templateEngine.process("login-template.html", context);
helper.setText(htmlContent, true);
```

**模板文件位置**: `src/main/resources/templates/login-template.html`

### 3. Spring Data Redis - 验证码存储

**核心操作**:
```java
// 存储验证码 + 设置过期时间（10分钟）
redisTemplate.opsForValue().set(key, value, 600, TimeUnit.SECONDS);

// 获取验证码
Object code = redisTemplate.opsForValue().get(key);

// 删除验证码（登录成功后）
redisTemplate.delete(key);
```

**Redis 连接配置**:
```yaml
spring:
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      database: 0
      timeout: 1000
      jedis:
        pool:
          max-active: 8       # 最大连接数
          max-wait: 5000      # 最大等待时间(ms)
          max-idle: 8         # 最大空闲连接
          min-idle: 0         # 最小空闲连接
```

### 4. Hutool - 工具库（可选优化）

当前代码使用原生 Java 生成随机数，可用 Hutool 简化:

```java
// 原生写法（当前代码）
Random random = new Random();
int num = random.nextInt(10);

// Hutool 写法（推荐）
import cn.hutool.core.util.RandomUtil;
String code = RandomUtil.randomNumbers(6);  // 直接生成6位随机数字符串
```

## API 接口

| 接口 | 方法 | 参数 | 说明 |
|------|------|------|------|
| `/login/code` | GET | `email` | 发送验证码到指定邮箱 |
| `/login/login` | GET | `email`, `code` | 验证码校验登录 |

**业务流程**:
1. 用户输入邮箱 → 调用 `/login/code`
2. 后端生成6位随机验证码 → 发送邮件 → 存入 Redis（10分钟过期）
3. 用户输入验证码 → 调用 `/login/login`
4. 后端从 Redis 取出验证码比对 → 返回结果

## 项目结构

```
src/main/java/com/example/email/
├── config/
│   └── RedisConfig.java              # Redis 序列化配置
├── controller/
│   ├── LoginController.java          # 登录接口（发码+验码）
│   └── RedisController.java          # Redis 操作接口
├── service/
│   ├── CodeService.java              # 验证码生成 + 邮件构建
│   └── RedisService.java             # Redis CRUD 封装
└── EmailApplication.java             # 启动类

src/main/resources/
├── application.yml                   # 配置文件
└── templates/
    ├── email-template.html           # 邮件模板1
    └── login-template.html           # 登录验证码邮件模板
```

## 快速启动

1. **配置邮箱**: 修改 `application.yml` 中的 `spring.mail.username` 和 `password`（授权码）
2. **启动 Redis**: 确保本地 Redis 服务运行在 `127.0.0.1:6379`
3. **运行项目**: 执行 `EmailApplication.main()`
4. **测试接口**:
   ```bash
   # 发送验证码
   curl "http://localhost:9090/login/code?email=your@email.com"
   
   # 验证登录
   curl "http://localhost:9090/login/login?email=your@email.com&code=123456"
   ```

## 注意事项

⚠️ **安全提醒**:
- 生产环境不要将邮箱授权码提交到代码仓库（建议使用环境变量或配置中心）
- 验证码应限制发送频率（防刷接口）
- 建议增加图形验证码或滑块验证作为前置防护
