# Spring Alipay — 支付宝沙箱支付完整集成

## 技术栈

| 项 | 选型 |
|---|---|
| JDK | 17 |
| Spring Boot | 3.4.4 |
| 支付宝 SDK | `alipay-sdk-java` 4.22.110.ALL |
| 支付宝 EasySDK | `alipay-easysdk` 2.2.0 |

## 用到的两个支付宝库，各司其职

### 1. alipay-sdk-java — 发起支付

**核心类：`DefaultAlipayClient` + `AlipayTradePagePayRequest`**

```java
AlipayClient client = new DefaultAlipayClient(
    gatewayUrl, appId, appPrivateKey,
    "json", charset, alipayPublicKey, signType
);

AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
request.setReturnUrl(returnUrl);
request.setNotifyUrl(notifyUrl);
request.setBizContent("{...}");

String formHtml = client.pageExecute(request).getBody();
// 直接将 formHtml 写入 response，浏览器自动提交表单跳转支付宝收银台
```

要点：
- `pageExecute()` 返回的是一段 **HTML 表单**，不是 URL
- 前端不需要额外处理，服务端直接 `response.getWriter().write(form)` 即可
- 产品码固定为 `FAST_INSTANT_TRADE_PAY`（即时到账）

### 2. alipay-easysdk — 异步通知验签

**核心方法：`Factory.Payment.Common().verifyNotify(params)`**

```java
Map<String, String> params = new HashMap<>();
request.getParameterMap().forEach((k, v) -> params.put(k, request.getParameter(k)));

if (Factory.Payment.Common().verifyNotify(params)) {
    // 验签通过 → 更新订单状态
    String tradeStatus = params.get("trade_status");
    String outTradeNo   = params.get("out_trade_no");
    String totalAmount  = params.get("total_amount");
}
return "success";  // 必须返回 "success" 字符串
```

要点：
- **必须先验签**再处理业务，防止伪造通知
- 验签通过后返回 `"success"`，支付宝才会停止重试
- 返回其他内容或超时无响应 → 支付宝会按策略重试通知（共 8 次）

## 为什么两个 SDK 混用？

| 能力 | alipay-sdk-java | alipay-easysdk |
|---|---|---|
| 发起支付（page/wap/app） | ✅ | ✅ |
| 异步通知验签 | 需手动拼 AlipaySignature | ✅ 一行搞定 |
| API 风格 | 原生 Request/Response | 工厂链式调用 |

本项目：**SDK 发起支付 + EasySDK 验签**，取两者最简用法。

## 接口一览

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/pay?outTradeNo=&subject=&totalAmount=` | 发起页面支付，输出 HTML 表单 |
| GET | `/callback` | 同步回调（用户支付完跳回） |
| POST | `/notify` | 异步通知（支付宝服务器回调，需验签） |

## 配置项（application.yml）

```yaml
alipay:
  appId:          # 应用 APPID
  appPrivateKey:  # 应用私钥
  alipayPublicKey:# 支付宝公钥
  notifyUrl:      # 异步通知地址（必须公网可访问）
  returnUrl:      # 同步返回地址
  gatewayUrl:     # 网关地址（沙箱 / 正式环境不同）
  charset:        utf-8
  signType:       RSA2
```

## 沙箱环境启动步骤

1. 登录 [支付宝开放平台](https://open.alipay.com) → 开发服务 → 研发服务 → 沙箱环境
2. 获取 `appId`、生成应用私钥/支付宝公钥
3. 下载 **沙箱版支付宝 App** 扫码登录（正式 App 无法使用沙箱）
4. 将 `gatewayUrl` 配置为沙箱网关：`https://openapi-sandbox.dl.alipaydev.com/gateway.do`
5. `notifyUrl` 需用内网穿透工具（ngrok / cpolar）暴露到公网
