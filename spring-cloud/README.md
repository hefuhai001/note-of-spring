# Spring Cloud Eureka 服务注册与发现

## 技术栈

| 组件 | 版本 | 说明 |
|------|------|------|
| Java | 17 | JDK 17 |
| Spring Boot | 3.2.3 | 基础框架 |
| Spring Cloud | 2023.0.0 | 微服务框架 |
| Netflix Eureka | - | 服务注册中心 |

## 核心依赖解析

### 1. Eureka Server（服务注册中心）

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

**作用**：提供服务注册与发现能力，所有微服务实例向其注册，并通过它发现其他服务。

**关键配置**（[Eureka/application.yml](Eureka/src/main/resources/application.yml)）：
```yaml
server:
  port: 8761  # Eureka 默认端口

eureka:
  client:
    registerWithEureka: false   # 不向自己注册（Server 模式必须关闭）
    fetchRegistry: false         # 不拉取注册表（Server 模式必须关闭）
  server:
    enableSelfPreservation: false # 关闭自我保护模式（开发环境建议关闭）
```

**启动注解**：
```java
@EnableEurekaServer  // 声明为 Eureka Server
```

---

### 2. Eureka Client（服务客户端）

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

**作用**：将当前服务注册到 Eureka Server，并具备服务发现能力。

**关键配置**（[Client/application.yml](Client/src/main/resources/application.yml)）：
```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/  # Eureka Server 地址
  instance:
    hostname: localhost  # 实例主机名

spring:
  application:
    name: client  # 服务名称（用于服务间调用）
```

**启动注解**：
```java
@EnableDiscoveryClient  // 启用服务发现客户端
```

---

### 3. Jersey 排除（重要）

```xml
<exclusions>
    <exclusion>
        <groupId>com.sun.jersey</groupId>
        <artifactId>jersey-client</artifactId>
    </exclusion>
    <!-- 其他 jersey 依赖 -->
</exclusions>
```

**原因**：Eureka Client 默认使用 Jersey 作为 HTTP 客户端，排除后改用 Spring MVC 的 RestTemplate，更符合 Spring 生态。

## 项目结构

```
spring-cloud/
├── Eureka/              # 服务注册中心
│   ├── pom.xml
│   └── src/main/java/.../EurekaApplication.java
└── Client/              # 客户端服务
    ├── pom.xml
    └── src/main/java/.../ClientApplication.java
```

## 启动顺序

1. **先启动 Eureka Server**：`http://localhost:8761`
2. **再启动 Client**：自动注册到 Eureka
3. **访问 Eureka 控制台**：浏览器打开 `http://localhost:8761` 查看注册的服务实例

## 关键点总结

- ✅ **单机模式**：Eureka Server 必须关闭 `registerWithEureka` 和 `fetchRegistry`
- ✅ **服务命名**：通过 `spring.application.name` 定义服务标识，用于服务间调用
- ✅ **HTTP 客户端**：排除 Jersey 使用 Spring MVC Rest 方式，避免依赖冲突
- ✅ **自我保护模式**：生产环境开启，开发环境可关闭以快速发现问题
