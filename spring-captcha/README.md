# Spring Captcha - 手写图形验证码

> 纯 Java AWT 实现，零第三方依赖，开箱即用

## 核心功能

| 功能 | 说明 |
|------|------|
| 随机字符生成 | 默认去除 `1,0,i,o` 易混淆字符 |
| 干扰线 + 噪点 | 20条干扰线 + 5%噪点密度 |
| 图片扭曲变形 | shearX/shearY 双向扭曲 |
| 输出流/文件双模式 | 支持 OutputStream / File 输出 |

## 快速开始

### 1. 添加依赖（无需额外依赖）

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### 2. 核心代码

**VerifyCodeUtils.java** - 验证码工具类：

```java
// 生成4位随机验证码
String code = VerifyCodeUtils.generateVerifyCode(4);

// 输出到响应流（推荐）
response.setContentType("image/png");
VerifyCodeUtils.outputImage(130, 60, response.getOutputStream(), code);

// 或输出到文件
VerifyCodeUtils.outputImage(130, 60, new File("captcha.jpg"), code);
```

**Controller 层调用：**

```java
@GetMapping("/generateImageCode")
public void generateImageCode(HttpSession session, HttpServletResponse response) throws IOException {
    String code = VerifyCodeUtils.generateVerifyCode(4);
    session.setAttribute("code", code);  // 存入Session用于校验
    response.setContentType("image/png");
    VerifyCodeUtils.outputImage(130, 60, response.getOutputStream(), code);
}
```

### 3. 前端使用

```html
<img src="/generateImageCode" onclick="this.src='/generateImageCode?'+new Date()" />
```

## API 接口

| 接口 | 方法 | 说明 |
|------|------|------|
| `/generateImageCode` | GET | 生成验证码图片，返回 image/jpeg |

**Swagger 文档地址：**
- Swagger UI: `http://localhost:9090/swagger-ui/index.html`
- Knife4j: `http://localhost:9090/doc.html`

## 参数说明

### VerifyCodeUtils 方法签名

```java
// 生成验证码字符串
String generateVerifyCode(int verifySize)
String generateVerifyCode(int verifySize, String sources)

// 生成并返回验证码值（自动写入输出流）
String outputVerifyImage(int w, int h, OutputStream os, int verifySize)
String outputVerifyImage(int w, int h, File outputFile, int verifySize)

// 使用指定code生成图片
void outputImage(int w, int h, OutputStream os, String code)
void outputImage(int w, int h, File outputFile, String code)
```

**参数说明：**

| 参数 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| w | int | 130 | 图片宽度 |
| h | int | 60 | 图片高度 |
| verifySize | int | 4 | 验证码长度 |
| sources | String | `23456789ABCDEFGHJKLMNPQRSTUVWXYZ` | 自定义字符源 |

## 技术细节

### 字符集设计

```java
public static final String VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
```

**剔除原因：**
- `1` 与 `l` 混淆
- `0` 与 `O` 混淆
- `i` 与 `l` 混淆

### 安全特性

1. **随机背景色** - 每次生成不同色调
2. **20条干扰线** - 随机位置、长度、角度
3. **5%噪点密度** - 随机色块填充
4. **双向剪切变形** - X/Y轴正弦波扭曲
5. **字符旋转** - ±45°随机旋转

### Session 校验流程

```
前端请求 → 后端生成code → 存入Session → 返回图片
                                        ↓
用户输入 → 提交表单 → 从Session取code → 对比校验 → 返回结果
```

## 项目配置

**application.yml 关键配置：**

```yaml
server:
  port: 9090

knife4j:
  enable: true
  setting:
    language: zh_cn
```

## 扩展建议

### 1. 添加 Redis 存储（分布式场景）

```java
@Autowired
private StringRedisTemplate redisTemplate;

// 替代Session
String uuid = UUID.randomUUID().toString();
redisTemplate.opsForValue().set("captcha:" + uuid, code, 5, TimeUnit.MINUTES);
```

### 2. 自定义字符集

```java
// 纯数字
String code = VerifyCodeUtils.generateVerify(6, "0123456789");

// 数学运算
String code = VerifyCodeUtils.generateVerify(1, "+-*");
```

### 3. 调整复杂度

修改 [VerifyCodeUtils.java](src/main/java/com/example/hfh/utils/VerifyCodeUtils.java)：
- 干扰线数量：第155行 `i < 20`
- 噪点率：第162行 `yawpRate = 0.05f`
- 扭曲强度：第175行 `Math.PI / 4`

## 常见问题

**Q: 图片显示乱码？**
A: 确保系统安装了 `Algerian` 字体，或替换为系统自带字体如 `Arial`

**Q: 验证码不刷新？**
A: URL加时间戳参数：`/generateImageCode?t=${Date.now()}`

**Q: 如何提高安全性？**
A: 建议结合 Redis + 限制频率 + 过期时间机制

## 项目结构

```
spring-captcha/
├── src/main/java/com/example/captcha/
│   ├── config/          # 全局异常处理、Swagger配置
│   ├── controller/      # 验证码接口
│   ├── resp/            # 统一响应封装
│   └── utils/           # 核心验证码工具类
└── src/main/resources/
    └── application.yml
```

## 技术栈

- **框架**: Spring Boot 3.0.6 + JDK 17
- **文档**: Knife4j (OpenAPI 3)
- **数据库**: MySQL + MyBatis（预留）
- **核心**: java.awt.Graphics2D（无额外依赖）

---

**适用场景**: 登录注册、防刷接口、人机验证等需要图形验证码的场景
