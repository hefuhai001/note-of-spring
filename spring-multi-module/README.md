# Spring Boot 多模块工程实战

> 技术栈：**Spring Boot 3.1.0** | **Java 17** | **Maven 多模块**

---

## 项目结构

```
parent-module/                  # 父 POM（统一版本管理）
├── core-module/                # 核心模块：公共异常、工具类、Domain
├── business-module/            # 业务模块：Controller / Service（端口 8000）
└── integration-test-module/    # 集成测试模块：端到端测试
```

依赖方向：`integration-test → business → core`（单向依赖，杜绝循环引用）

---

## 核心第三方库一览

| 库 | 用途 | 所在模块 |
|---|---|---|
| `spring-boot-starter-web` | 内嵌 Tomcat + REST 支持（`@RestController`、`@GetMapping`） | business-module |
| `spring-boot-starter` | 自动配置、IOC 容器、日志（默认 Logback） | core-module |
| `spring-boot-starter-test` | JUnit 5 + MockMvc + AssertJ | integration-test-module |
| `spring-boot-dependencies` | **BOM**：集中管理 Spring 全家桶版本，子模块免写 version | parent-module |
| `spring-boot-maven-plugin` | 打包可执行 jar / repackage 阶段 | parent-pluginManagement |

### 为什么用 BOM（`spring-boot-dependencies`）？

```xml
<!-- 父 POM 统一声明版本 -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>${spring-boot.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<!-- 子模块直接用，不用写 version -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

**好处**：全局版本一致，升级只需改一处。

---

## 模块职责与关键代码

### 1. core-module — 公共异常定义

[CustomException.java](parent-module/core-module/src/main/java/com/example/core/exception/CustomException.java)

```java
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
```

运行时异常，上层业务直接 `throw new CustomException("msg")`，配合 `@ControllerAdvice` 做全局异常捕获（本示例未展开）。

### 2. business-module — REST 接口层

[HelloController.java](parent-module/business-module/src/main/java/com/example/business/controller/HelloController.java)

- `/hello` — 抛出自定义异常（演示异常传播）
- `/test` — 正常返回字符串

端口配置在 [application.properties](parent-module/business-module/src/main/resources/application.properties)：
```properties
server.port=8000
```

### 3. integration-test-module — 端到端测试

依赖 `business-module`，使用 `spring-boot-starter-test` 进行集成测试。

---

## 启动方式

```bash
cd parent-module
mvn clean install -DskipTests      # 先构建所有模块
cd business-module
mvn spring-boot:run                 # 启动业务模块
```

访问 `http://localhost:8000/test` 验证。

---

## 架构要点总结

- **分层隔离**：core 只依赖 starter，不依赖 web；business 依赖 core；test 依赖 business
- **版本收敛**：父 POM 用 `<dependencyManagement>` 锁定 Spring Boot 版本
- **插件管理**：`<pluginManagement>` 让子模块按需继承 `spring-boot-maven-plugin`
- **测试独立成模块**：集成测试代码与业务代码物理分离，CI 可单独控制执行
