# Spring Boot 集成支付宝支付（沙箱环境）

> 基于 `alipay-sdk-java` + `hutool` 的极简实现方案

## 技术栈

| 依赖 | 版本 | 用途 |
|------|------|------|
| alipay-sdk-java | 4.22.110.ALL | 支付宝官方SDK |
| hutool-all | 5.7.20 | JSON处理 |

```xml
<dependency>
    <groupId>com.alipay.sdk</groupId>
    <artifactId>alipay-sdk-java</artifactId>
    <version>4.22.110.ALL</version>
</dependency>

<dependency>
    <groupId>cn.hutool</groupId>
    <artifactId>hutool-all</artifactId>
    <version>5.7.20</version>
</dependency>
```

## 核心配置

### application.yml

```yaml
alipay:
    appId: 2016102600763473
    appPrivateKey: MIIEwAIBADANBgkqhkiG9w0BAQEFAASCBKowggSmAgEAAoIBAQDDVLnhmbCGYwRfe+8K0KPVyxeZIXlK095HAW9Aiuq9tb3ikDh4zlxoJzmv+rUZtRQestoo7PCYNUTb2vaDOv9WaJ+vfkPSHl7+rx0PsG0fFPeIRyTHTQtjpLBEq1kuXojcnSCO41rhygmb3MJNYEZTLY+BdeaTMzWLSaL0O/ivmM4OvvhWdEhHUL7kotWL5b+jXAxu4bfJvlijAHt/7M7S3XPs70WfZpv6bYtnsz2tS0J7p3rVXVeYHIClIMpCgDUKzg2+O6CNoQBURLMjUNgcd8dYPU/i3ZbmDN2tAbm4qAaZAFNvvixkiQ8GmbXfQjQdrHRXsndWhaXsrIZrFHTHAgMBAAECggEBAK+QgrZUZkaTzwVnpnZpCykJLv+zB4sLHVli37T6Z3z2UmLGUAu9J29x+jbDN22QxTHGfbGVLbcRAgA+MZ3INGJdDVI2Qg0kwStXB97nK11pggcaMuNzy03XY5uFC1ZTH95y2CW2EiSpbLOL25SiePMgG0E4UHNz7qdwGyg8kwx3zgIKgp4GEbqCwAn+bXyI2svvql53nBzE0x6OPmUNWu+8OuysEtWLwjjDff9ft9pGb4+QUPdxbp0rqE3dEe5FwirsLeEodg2R/Yi3m2192pMyMeLf3rd38trJ8nmb6JlnKGzST23BtEB2ia6gVOtQHS703WrrCx6iICCVUYHwDCECgYEA+oA/rikrDi6pe76rtMZ5rVDRPaha/xfcW9vMLsoRLb7YuEP/BwbmUNq5TtM4BgSCSrTD6XUg0wPSX4BijvgeqFpBJAvQ90kTEfXGZnr2r2f635Bfo02mQr5IhyhPIKMJaM5wQVW1Op+Lx+eaa4j2BwcWNzt7F6AV/qPBwvPYHLECgYEAx55vn7z92fBfa7yNMm3XKNFxXo6uNN70iD5MyzdcTPuqz9R9UlCo1XIJGgV3C1bK4X2f+zFijtm8tqor+Q1N0j1hZKFW/YlF21/FPP6JPjniax/phegRwZ5jEuI/YF44YvkMbO6yPNqxsR+JO9lhgiW/8gnnmVDQIYRNjTfWpvcCgYEAuj6cGgr1vgenbx+mXjOqx9dsmqEPdtXwukNDHg6SkyZvzyCO/lR87OSSHi8gWikEDjMz7eFt1DXlNagonw+PC7B++iPm69Ri31mSdyM5QdTXS1z2Hl5fHQSIvCSWIDfXiRrjj0///GQe8zQZNZaRBUyZkdshe8FEWRy7tQzDQyECgYEAgYYQL7nVjUAm6iUiCQK0hUvBH/W8m5m/WVfzRDjbryftIYVi+7JSmoyv0y6Qm87pPX7h+3+Dz+UAShYJCkTTpgMl2sHFTCVyKnHt7THLo5CzlYbTY4u9WcCH0Iz1SnZYZ//pTBVlmY7dlWw9A5R9bJFKBqbem+CP6++I0oTUxukCgYEApmooMXYoXPIbEHiWxGzUEOFaOsGJaYEOIcJjbUqB+FoS2wFdPdKyomjwOF83+j3orPEL5SSXbMIRyxZNcuJAvXeEwJ6byx6d6txli1vfVfcfgn8YIaCZdYTgRTNGYYC4c6WrQ5Rt1kWO4oTVPPAsk2WT7sD99PpIFer5y3ICbkw=
    alipayPublicKey: MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmydAaVLYbtvWKdaZhN0zDAi8sym6nfCwO18hQfZZOtwIFoR90+CkxNCOtoLOPC3+ksB6wwoqzhn33v4cTkzmHJjhlN9MFVhCYWIw4z0RkjvE1snlMi8F+lynPIv9kRUDnv5N9tabagfmouuhJZ1Ly145yT+2MBOy20Jueaqj01xFak+kzgjqK4K/8Cid4kfLCj7t8btiOFWexfXy5ZLJHOsOvaiyrwkI7+pDe9eKiEwZQ7ixqwO6PSQsVf1swOjZMi30Lj8RcfsfrH9XAx7X2t1Qj945QEcCnT725gBqEJUmUb9bCpD9ioSas/USNqFMgS/iJ8n0gnAK1N8vueSykQIDAQAB
    notifyUrl: http://muaqx9.natappfree.cc/alipay/notify
    returnUrl: http://localhost:8080/orders
```

**配置项说明**：
- `appId`：应用ID（沙箱环境在[支付宝开放平台](https://open.alipay.com/develop/sandbox/app)获取）
- `appPrivateKey`：应用私钥（用于请求签名）
- `alipayPublicKey`：支付宝公钥（用于验签回调）
- `notifyUrl`：异步通知地址（必须外网可访问，需配合内网穿透工具如[natapp](https://natapp.cn/)）
- `returnUrl`：同步跳转地址（用户支付完成后跳转的页面）

### 配置类 AliPayConfig.java

```java
@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {
    private String appId;
    private String appPrivateKey;
    private String alipayPublicKey;
    private String notifyUrl;
    private String returnUrl;
}
```

## 核心实现

### 请求参数对象 AliPay.java

```java
@Data
public class AliPay {
    private String traceNo;        // 商户订单号
    private double totalAmount;    // 订单金额
    private String subject;        // 商品名称
    private String alipayTraceNo;  // 支付宝交易号（回调时返回）
}
```

### 支付接口 AliPayController.java

```java
@RestController
@RequestMapping("/alipay")
public class AliPayController {

    private static final String GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "UTF-8";
    private static final String SIGN_TYPE = "RSA2";

    @Resource
    private AliPayConfig aliPayConfig;

    @Resource
    private OrdersMapper ordersMapper;

    /**
     * 发起支付 - 返回支付宝表单页面
     * GET /alipay/pay?traceNo=xxx&totalAmount=100&subject=商品名
     */
    @GetMapping("/pay")
    public void pay(AliPay aliPay, HttpServletResponse httpResponse) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient(
            GATEWAY_URL, 
            aliPayConfig.getAppId(),
            aliPayConfig.getAppPrivateKey(), 
            FORMAT, 
            CHARSET, 
            aliPayConfig.getAlipayPublicKey(), 
            SIGN_TYPE
        );

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        request.setReturnUrl(aliPayConfig.getReturnUrl());

        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", aliPay.getTraceNo());
        bizContent.set("total_amount", aliPay.getTotalAmount());
        bizContent.set("subject", aliPay.getSubject());
        bizContent.set("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());

        String form = alipayClient.pageExecute(request).getBody();
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    /**
     * 支付宝异步通知回调 - 必须是POST接口
     * POST /alipay/notify
     */
    @PostMapping("/notify")
    public String payNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            Map<String, String> params = new HashMap<>();
            Map<String,String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
            }

            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean checkSignature = AlipaySignature.rsa256CheckContent(
                content, sign, aliPayConfig.getAlipayPublicKey(), "UTF-8"
            );

            if (checkSignature) {
                String outTradeNo = params.get("out_trade_no");
                String alipayTradeNo = params.get("trade_no");

                QueryWrapper<Orders> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("order_id", outTradeNo);
                Orders orders = ordersMapper.selectOne(queryWrapper);

                if (orders != null) {
                    orders.setAlipayNo(alipayTradeNo);
                    orders.setPayTime(new Date());
                    orders.setState("已支付");
                    ordersMapper.updateById(orders);
                }
            }
        }
        return "success";
    }
}
```

## 关键流程解析

### 1️⃣ 发起支付流程

```
前端调用 → GET /alipay/pay?参数 → 构建AlipayClient → 设置业务参数 → pageExecute生成表单 → 输出HTML到浏览器 → 自动跳转支付宝收银台
```

**核心API**：`DefaultAlipayClient.pageExecute()` - 将业务参数封装为支付宝表单

**必填参数**：
| 参数 | 说明 | 示例 |
|------|------|------|
| out_trade_no | 商户订单号 | 唯一，自定义生成 |
| total_amount | 订单金额 | 单位：元 |
| subject | 商品名称 | 显示在支付宝页面上 |
| product_code | 销售产品码 | 固定值：FAST_INSTANT_TRADE_PAY |

### 2️⃣ 异步回调流程

```
支付宝服务器 → POST /alipay/notify → 验证签名(RSA2) → 更新订单状态 → 返回"success"
```

**验签机制**：使用 `AlipaySignature.rsa256CheckContent()` 确保请求来自支付宝

**回调关键字段**：
- `trade_status`：交易状态（TRADE_SUCCESS 表示成功）
- `trade_no`：支付宝交易号
- `out_trade_no`：商户订单号
- `gmt_payment`：付款时间
- `total_amount`：交易金额

**⚠️ 注意事项**：
- 回调接口必须是 **POST** 方法
- 处理成功必须返回字符串 `"success"`（其他值会触发支付宝重试）
- `notifyUrl` 必须**外网可访问**（本地开发需内网穿透）

## 前端调用方式

```javascript
// 打开支付页面
window.open(`http://localhost:9090/alipay/pay?traceNo=${orderNo}&totalAmount=${price}&subject=${name}`)
```

## 沙箱测试账号

| 角色 | 账号 | 密码 |
|------|------|------|
| 买家 | xjlugv6874@sandbox.com | 在沙箱控制台查看 |
| 卖家 | lsagyy3417@sandbox.com | 在沙箱控制台查看 |

> 💡 沙箱地址：https://open.alipay.com/develop/sandbox/app  
> 📖 官方文档：https://opendocs.alipay.com/open/270/01didh

## 常见问题

### Q: 本地开发如何接收支付宝回调？
**A**: 使用内网穿透工具（natapp/frp/ngrok）将本地端口映射到公网，配置 `notifyUrl` 为映射后的地址。

### Q: 如何区分同步跳转和异步通知？
**A**: 
- **returnUrl（同步）**：用户支付完成后浏览器跳转，不可靠（可能用户关闭页面）
- **notifyUrl（异步）**：支付宝服务器主动调用，可靠（会重试），以这个为准更新订单状态

### Q: 签名失败怎么排查？
**A**: 检查以下几点：
1. 公私钥是否配对（应用私钥 vs 支付宝公钥）
2. 字符编码是否统一（建议 UTF-8）
3. 参数是否完整（不能遗漏必填字段）
