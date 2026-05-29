# Spring Boot Actuator 实战指南

## 一句话说明

Spring Boot Actuator = 生产环境下的**应用内窥镜**，通过 HTTP/JMX 暴露运行时指标、健康状态、环境信息等。

## 快速接入

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

一个依赖，开箱即用。启动后访问 `http://localhost:8000/actuator` 即可看到可用端点。

## 核心端点速查表

| 端点 | 用途 | 关键返回 |
|------|------|----------|
| `/actuator/health` | 健康检查 | status: UP/DOWN, 磁盘/数据库/Redis组件状态 |
| `/actuator/metrics` | 运行指标列表 | jvm.memory.used, http.server.requests, system.cpu.usage 等 |
| `/actuator/metrics/{name}` | 指标明细 | 可带 tag 过滤：`?tag=uri:/api/user` |
| `/actuator/env` | 环境配置 | 全部 property source，含 OS 环境变量、配置文件 |
| `/actuator/beans` | Bean 清单 | 所有 Spring Bean 的类型、作用域、依赖 |
| `/actuator/mappings` | 路由映射 | 所有 @RequestMapping 映射关系 |
| `/actuator/loggers` | 日志级别管理 | GET 查看 / POST 动态修改级别（无需重启）|
| `/actuator/info` | 自定义应用信息 | 通过 `info.*` 配置项自定义 |
| `/actuator/prometheus` | Prometheus 抓取 | Micrometer 格式的指标数据 |

## 必配 YAML

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus,loggers  # 按需开放，别用 "*"
  endpoint:
    health:
      show-details: when_authorized   # 生产用 when_authorized，开发用 always
```

**生产安全原则**：`include: "*"` 仅限本地开发。生产环境按最小权限原则只开需要的端点。

## 实战场景

### 场景1：健康检查 + 组件探活

```bash
curl http://localhost:8000/actuator/health
```

返回示例：
```json
{
  "status": "UP",
  "components": {
    "db": { "status": "UP", "details": { "database": "H2" } },
    "diskSpace": { "status": "UP", "details": { "total": 499963174912, "free": 184968808448 } },
    "ping": { "status": "UP" }
  }
}
```

K8s livenessProbe / readinessProbe 直接对接此接口即可。

### 场景2：Prometheus 监控集成

pom 加依赖：

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
    <scope>runtime</scope>
</dependency>
```

YAML 配置：

```yaml
management:
  prometheus:
    metrics:
      export:
        enabled: true
```

Prometheus 配置抓取目标：

```yaml
scrape_configs:
  - job_name: 'spring-app'
    metrics_path: '/actuator/prometheus'
    static_configs:
      - targets: ['localhost:8000']
```

Grafana 导入 **JVM (Micrometer)** dashboard（ID: 4701）直接出图。

### 场景3：线上动态调日志级别

```bash
# 查看当前日志级别
curl http://localhost:8000/actuator/loggers/com.example.demo

# 动态调整为 DEBUG（立即生效，不重启）
curl -X POST -H 'Content-Type: application/json' \
  -d '{"configuredLevel":"DEBUG"}' \
  http://localhost:8000/actuator/loggers/com.example.demo

# 恢复默认
curl -X POST -H 'Content-Type: application/json' \
  -d '{"configuredLevel":null}' \
  http://localhost:8000/actuator/loggers/com.example.demo
```

排查线上问题神器，改完记得关掉。

### 场景4：自定义 Health Indicator

```java
@Component
public class CustomHealthIndicator implements HealthIndicator {
    @Override
    public Health health() {
        int errorCode = check();  // 你的检测逻辑
        if (errorCode != 0) {
            return Health.down().withDetail("Error Code", errorCode).build();
        }
        return Health.up().build();
    }

    private int check() { return 0; }
}
```

访问 `/actuator/health` 会自动聚合你的组件状态。

### 场景5：自定义 Metrics

```java
private final Counter orderCounter;

public OrderService(MeterRegistry registry) {
    this.orderCounter = registry.counter("order.created", "type", "online");
}

public void createOrder() {
    orderCounter.increment();
}

// Timer 计耗时
private final Timer orderTimer;
public OrderService(MeterRegistry registry) {
    this.orderTimer = registry.timer("order.process.time");
}

public void processOrder() {
    orderTimer.record(() -> {
        // 业务逻辑
    });
}
```

在 Prometheus 里直接查 `order_created_total{type="online"}` 和 `order_process_time_seconds`。

### 场景6：查看某个指标明细

```bash
# 列出所有可用指标名
curl http://localhost:8000/actuator/metrics

# 查看特定指标的 tag 维度
curl http://localhost:8000/actuator/metrics/jvm.memory.used

# 按 tag 过滤取值
curl "http://localhost:8000/actuator/metrics/http.server.requests?tag=method:GET&tag=uri:/api/users"
```

## 安全加固（生产必做）

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

```java
@Configuration
public class ActuatorSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.requestMatcher(EndpointRequest.toAnyEndpoint())
            .authorizeHttpRequests(auth -> auth
                .hasRole("ACTUATOR_ADMIN")
            )
            .httpBasic(withDefaults());
        return http.build();
    }
}
```

或者简单粗暴——**改 context-path + 端口隔离**：

```yaml
management:
  server:
    port: 8081          # 管理端口独立
  endpoints:
    web:
      base-path: /admin # 改路径防扫描
```

## 常见坑

| 问题 | 原因 | 解决 |
|------|------|------|
| `/actuator` 返回空 `{}` | 默认只暴露 `health` 和 `info` | `endpoints.web.exposure.include=*` |
| `/health` 不显示组件详情 | 默认隐藏详情 | `endpoint.health.show-details=always` |
| Prometheus 无数据 | 缺 micrometer 依赖或未启用 export | 加依赖 + 配 `enabled: true` |
| 安全框架拦截了 actuator 路径 | Spring Security 默认保护所有端点 | 排除 `/actuator/**` 或单独鉴权 |

## 架构图

```
┌─────────────────────────────────────────────┐
│              Spring Boot App                 │
│                                             │
│  ┌──────────┐  ┌───────────┐  ┌──────────┐ │
│  │ Web 层   │  │ Service层 │  │  数据层   │ │
│  └────┬─────┘  └─────┬─────┘  └────┬─────┘ │
│       │              │              │        │
│  ┌────▼──────────────▼──────────────▼─────┐ │
│  │           Actuator AutoConfigure       │ │
│  │                                         │ │
│  │  HealthIndicator  │  MeterRegistry     │ │
│  │  (组件探活)       │  (指标采集)        │ │
│  └────────┬──────────┴──────────┬─────────┘ │
│           │                     │           │
│  ┌────────▼────────────────────▼─────────┐  │
│  │         HTTP Endpoint (/actuator/*)    │  │
│  └────────────────────┬──────────────────┘  │
└───────────────────────┼─────────────────────┘
                        │
           ┌────────────┼────────────┐
           ▼            ▼            ▼
      Prometheus    K8s Probe    运维排查
      (监控告警)    (健康检查)    (日志/配置)
```
