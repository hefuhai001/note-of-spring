# Spring Cloud 微服务实战

基于 **Spring Boot 3.1.4 + Spring Cloud 2022.0.4 + Java 17** 的微服务脚手架项目。

## 架构总览

```
                    ┌─────────────┐
                    │   Client    │
                    └──────┬──────┘
                           │ HTTP
                    ┌──────▼──────┐
                    │ API Gateway │ :8080
                    │  (Gateway)  │
                    └──────┬──────┘
                           │ 路由转发
              ┌────────────┼────────────┐
              │            │            │
       ┌──────▼──────┐ ┌──▼───┐ ┌──────▼──────┐
       │  Service A  │ │ ...  │ │  Service B  │
       └─────────────┘ └─────┘ └─────────────┘
              │            │            │
              └────────────┼────────────┘
                           │ 注册/发现
                    ┌──────▼──────┐
                    │ Eureka Server│ :8761
                    └─────────────┘

              ┌──────────────────────┐
              │    Config Server     │ :8888
              │   (Native File System)│
              └──────────────────────┘
```

## 模块说明

| 模块 | 端口 | 职责 |
|------|------|------|
| `eureka-server` | 8761 | 服务注册中心 |
| `config-server` | 8888 | 配置中心（本地文件系统） |
| `api-gateway` | 8080 | API 网关，统一入口 |
| `service-a` | 由配置中心分配 | 业务服务 A |
| `service-b` | 由配置中心分配 | 业务服务 B |

## 启动顺序

```
Eureka Server → Config Server → Service A/B → API Gateway
```

## 第三方库详解

### 1. Spring Cloud Netflix Eureka — 服务注册与发现

**核心作用**：去中心化的服务注册表，每个服务启动时向 Eureka 注册自己的 IP 和端口，同时拉取其他服务的列表。

```yaml
# eureka-server: 服务端关闭自注册（单机模式）
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false

# 客户端：指定注册地址即可自动注册
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

**关键点**：
- Eureka 采用 **AP 设计**（可用性 > 一致性），节点间异步复制，容忍网络分区
- 客户端有**本地缓存**，即使 Eureka 挂了仍能调用已知服务
- 默认 30s 心跳续约、90s 剔除失联服务

### 2. Spring Cloud Config — 分布式配置中心

**核心作用**：将各服务的 `application.yml` 集中管理，支持运行时动态刷新。

本项目使用 **Native 模式**（本地文件系统），配置文件存放在 `config-server/src/main/resources/config/` 下：

```
config/
├── application.yml      # 公共配置
├── api-gateway.yml      # 网关专属配置
├── service-a.yml        # 服务A专属配置
└── service-b.yml        # 服务B专属配置
```

客户端通过 `bootstrap.yml` 连接配置中心（bootstrap 加载优先于 application）：

```yaml
spring:
  application:
    name: service-a        # 对应 service-a.yml
  cloud:
    config:
      uri: http://localhost:8888
      fail-fast: false
      retry:
        initial-interval: 1000
        max-attempts: 3
```

**动态刷新**：配合 Actuator 的 `/actuator/refresh` 端点，无需重启即可生效（需在类上加 `@RefreshScope`）。

### 3. Spring Cloud Gateway — API 网关

**核心作用**：系统的统一入口，负责路由转发、负载均衡、鉴权等横切关注点。

**技术选型关键**：Spring Cloud Gateway 基于 **Netty + WebFlux（响应式编程）**，不是传统 Servlet 容器。所以：
- 不能用 `spring-boot-starter-web`
- 路由配置用 `RouteLocator` 或 YAML
- 过滤器分 `GatewayFilter`（单路由）和 `GlobalFilter`（全局）

```yaml
# 典型路由配置示例
spring:
  cloud:
    gateway:
      routes:
        - id: service-a
          uri: lb://service-a          # lb:// 表示从 Eureka 负载均衡
          predicates:
            - Path=/api/service-a/**
          filters:
            - StripPrefix=1            # 去掉一级前缀再转发
```

**lb:// 协议**：Gateway 与 Eureka Client 配合使用时，直接用服务名路由，内置 Ribbon 负载均衡。

### 4. Spring Boot Actuator — 运维监控端点

**核心作用**：暴露生产级监控指标，所有模块均已引入。

| 端点 | 用途 |
|------|------|
| `/actuator/health` | 服务健康状态 |
| `/actuator/info` | 自定义应用信息 |
| `/actuator/env` | 当前环境变量和配置 |
| `/actuator/refresh` | 刷新配置（Config Client 专用） |

本项目中 Config Server 显式暴露了 `health,info,env`：

```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,env
```

### 5. Spring Retry — 声明式重试机制

**核心作用**：对可能失败的操作（如连接 Config Server）进行自动重试，避免瞬时故障导致启动失败。

```yaml
spring:
  cloud:
    config:
      retry:
        initial-interval: 1000    # 首次重试间隔 1s
        max-attempts: 3           # 最大重试 3 次
        max-interval: 2000        # 最大间隔 2s
        multiplier: 1.1           # 间隔递增倍数
```

**代码中使用**：配合 `@Retryable` 注解或模板方式：

```java
@Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
public String callRemoteService() { ... }
```

> 注意：`@Retryable` 需要 AOP 支持，这也是为什么 service-a/b 引入了 `spring-aspects` + `aspectjweaver`。

### 6. AspectJ / Spring AOP — 面向切面编程

Service A 和 B 引入了 AspectJ 相关依赖，用于支撑：
- `@Retryable` 的代理拦截
- 自定义日志、权限校验等横切逻辑

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aspects</artifactId>
</dependency>
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
</dependency>
```

## 技术选型总结

| 组件 | 选型 | 替代方案 |
|------|------|----------|
| 注册中心 | Netflix Eureka | Consul、Nacos、Zookeeper |
| 配置中心 | Spring Cloud Config (Native) | Nacos、Apollo、Consul KV |
| 网关 | Spring Cloud Gateway | Zuul 1.x（已停更）、Kong |
| 服务调用 | REST Template / Feign（已预留） | gRPC、Dubbo |
| 重试 | Spring Retry | Resilience4j（推荐替代） |
