# Spring 工厂模式实战

基于 Spring IoC 容器自动注入 Map 特性，零配置实现支付工厂。

## 技术栈

| 组件 | 版本 |
|------|------|
| Spring Boot | 3.5.3 |
| Java | 17 |
| Knife4j (OpenAPI3) | 4.5.0 |

## 核心原理

Spring 注入 `Map<String, XxxInterface>` 时，会自动将容器中所有 `XxxInterface` 的 Bean 收集进来，key 就是 `@Service("xxx")` 中指定的名称。

```java
@Component
public class PaymentFactory {

    @Autowired
    private Map<String, PaymentService> paymentMap;

    public PaymentService getService(String type) {
        PaymentService service = paymentMap.get(type.toLowerCase());
        if (service == null) {
            throw new IllegalArgumentException("暂不支持的支付类型：" + type);
        }
        return service;
    }
}
```

## 新增支付方式只需两步

**1. 实现 `PaymentService` 接口**

```java
@Service("unionpay")
public class UnionpayServiceImpl implements PaymentService {
    @Override
    public String pay(BigDecimal amount) {
        return "使用银联支付：" + amount + " 元";
    }
}
```

**2. 完成。** 无需修改 Factory，无需任何配置，Spring 启动时自动注册。

## 接口调用

```
GET /pay?type=alipay&amount=100
```

| type | 说明 |
|------|------|
| alipay | 支付宝 |
| wechat | 微信支付 |
| unionpay | 银联 |

## API 文档

启动后访问 `http://localhost:8080/doc.html` 查看 Knife4j 文档。

## 项目结构

```
src/main/java/com/hfh/factory/
├── controller/
│   └── PayController.java          # 入口
├── factory/
│   └── PaymentFactory.java         # 核心：Map自动注入
└── service/
    ├── PaymentService.java         # 接口
    └── impl/
        ├── AlipayServiceImpl.java
        ├── WechatServiceImpl.java
        └── UnionpayServiceImpl.java
```

## 为什么不用 if-else 或 switch

传统写法每加一个支付方式都要改 Factory 代码，违反开闭原则。本方案利用 Spring 容器特性，**新增策略零改动**。
