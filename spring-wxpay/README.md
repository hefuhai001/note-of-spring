# Spring Boot 微信支付集成方案

> 基于 Spring Boot 3.x + WxJava 实现的微信支付快速集成 Demo，涵盖统一下单与异步通知处理。

## 📌 前言

在电商、SaaS 等业务场景中，微信支付是必不可少的支付渠道。本文将介绍如何使用 **Spring Boot** 结合 **WxJava**（微信开发 Java SDK）快速搭建微信支付服务，实现从下单到回调通知的完整流程。

## 🛠 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | LTS 版本 |
| Spring Boot | 3.4.4 | 最新稳定版 |
| WxJava (weixin-java-pay) | 4.1.0 | 微信支付 SDK |
| Maven | - | 构建工具 |

## ✨ 功能特性

- ✅ **统一下单**：支持 JSAPI/NATIVE/APP 等多种支付方式
- ✅ **异步通知**：处理微信支付回调，自动验签
- ✅ **订单查询**：主动查询订单状态
- ✅ **配置化**：通过 YAML 统一管理支付参数

## 📁 项目结构

```
spring-wxpay/
├── src/main/java/com/example/wxpay/
│   ├── config/
│   │   └── WxPayConfiguration.java    # 支付配置类
│   ├── controller/
│   │   ├── WxPayController.java       # 下单接口
│   │   └── WxPayNotifyController.java # 回调处理
│   └── SpringWxpayApplication.java    # 启动类
├── src/main/resources/
│   ├── application.yml                # 配置文件
│   └── static/index.html              # 演示页面
└── pom.xml
```

## 🚀 快速开始

### 1️⃣ 环境准备

确保已安装：
- JDK 17+
- Maven 3.6+

### 2️⃣ 配置参数

编辑 `src/main/resources/application.yml`，填入你的微信商户信息：

```yaml
wxpay:
  appId: wx1234567890abcdef        # 公众号/小程序 AppID
  mchId: 1234567890                 # 商户号
  mchKey: your_mch_key              # 商户密钥（APIv2）
  keyPath: classpath:/cert/apiclient_cert.p12  # 商户证书路径
  notifyUrl: https://yourdomain.com/notify     # 回调地址（必须 HTTPS）
```

> ⚠️ **注意**：生产环境请将证书文件放入 `resources/cert/` 目录，并确保回调地址已备案且支持 HTTPS。

### 3️⃣ 启动项目

```bash
# 方式一：Maven 命令行
mvn spring-boot:run

# 方式二：打包后运行
mvn package -DskipTests
java -jar target/spring-wxpay-0.0.1-SNAPSHOT.jar
```

项目启动后访问：`http://localhost:8080`

## 🔌 API 接口说明

### 1. 创建支付订单

**接口地址**：`POST /pay/createOrder`

**请求示例**：
```bash
curl -X POST http://localhost:8080/pay/createOrder
```

**核心代码逻辑** [WxPayController.java](src/main/java/com/example/wxpay/controller/WxPayController.java)：

```java
@PostMapping("/createOrder")
public String createOrder() throws WxPayException {
    WxPayUnifiedOrderRequest request = new WxPayUnifiedOrderRequest();
    request.setBody("测试商品");           // 商品描述
    request.setOutTradeNo("1234567890");   // 商户订单号（唯一）
    request.setTotalFee(1);               // 金额（单位：分）
    request.setSpbillCreateIp("127.0.0.1");
    request.setTradeType("JSAPI");         // 支付类型
    request.setOpenid("用户的openid");
    
    WxPayUnifiedOrderResult result = wxPayService.unifiedOrder(request);
    return result.getPrepayId();          // 返回预支付ID
}
```

### 2. 支付结果通知

**接口地址**：`POST /notify/payNotify`

**处理流程** [WxPayNotifyController.java](src/main/java/com/example/wxpay/controller/WxPayNotifyController.java)：

```java
@PostMapping("/payNotify")
public String payNotify(@RequestBody String xmlData) throws WxPayException {
    // 1. 解析并验签
    WxPayOrderNotifyResult notifyResult = wxPayService.parseOrderNotifyResult(xmlData);
    
    // 2. 获取订单号
    String outTradeNo = notifyResult.getOutTradeNo();
    
    // 3. 主动查询确认（防止伪造通知）
    WxPayOrderQueryRequest queryRequest = new WxPayOrderQueryRequest();
    queryRequest.setOutTradeNo(outTradeNo);
    WxPayOrderQueryResult queryResult = wxPayService.queryOrder(queryRequest);
    
    // 4. 处理业务逻辑（更新订单状态、发货等）
    
    // 5. 返回成功响应（必须返回此格式）
    return "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
}
```

## 💡 核心实现解析

### 配置注入机制

通过 Spring 的 `@Configuration` + `@Value` 实现配置的外部化管理：

[WxPayConfiguration.java](src/main/java/com/example/wxpay/config/WxPayConfiguration.java)

```java
@Bean
public WxPayService wxPayService(WxPayConfig wxPayConfig) {
    WxPayService wxPayService = new WxPayServiceImpl();
    wxPayService.setConfig(wxPayConfig);  // 注入配置
    return wxPayService;
}
```

这种设计使得：
- 配置与业务代码解耦
- 支持多环境切换（dev/test/prod）
- 便于单元测试 Mock

### 安全注意事项

1. **回调验签**：必须调用 `parseOrderNotifyResult()` 自动验证签名
2. **二次查询**：收到通知后应主动查询微信订单状态，防止重复通知或伪造
3. **幂等性**：同一订单号的处理逻辑需保证幂等
4. **HTTPS**：回调地址必须是 HTTPS 且已备案域名

## 📊 支付类型对照表

| TradeType | 场景 | 必填参数 |
|-----------|------|----------|
| JSAPI | 公众号/小程序支付 | openid |
| NATIVE | 扫码支付 | - |
| APP | 移动端 APP | - |
| H5 | H5 页面支付 | scene_info |

## ❓ 常见问题

**Q：提示"商户号与 AppID 不匹配"？**
A：检查公众号/小程序绑定的商户号是否正确。

**Q：回调收不到通知？**
A：确认 notifyUrl 可公网访问，且服务器防火墙放行了 443 端口。

**Q：证书路径报错？**
A：将 `.p12` 文件放到 `resources/cert/` 目录下，或修改 `keyPath` 为绝对路径。

## 📝 后续优化方向

- [ ] 集成微信支付 V3 API（RSA 签名）
- [ ] 添加退款功能
- [ ] 对账单下载与对账
- [ ] 接入企业付款/商家转账
- [ ] 添加分布式锁防止重复处理

## 📄 License

MIT License

---

💡 **学习建议**：建议先在[微信支付沙箱环境](https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=23_1)中测试，熟悉流程后再接入生产环境。
