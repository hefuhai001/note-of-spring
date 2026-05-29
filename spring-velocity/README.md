# 🚀 Spring Velocity 代码生成器

> 基于 Spring Boot + Apache Velocity + Knife4j 构建的轻量级代码生成工具

## ✨ 项目简介

在日常开发中，我们经常需要编写大量重复性的样板代码——Entity、Controller、DTO... 这个项目旨在通过**模板引擎技术**，将这种机械性工作自动化。只需提供简单的参数，即可一键生成规范的 Java 代码。

本项目核心依赖两个优秀的开源库：

| 库名 | 版本 | 用途 |
|------|------|------|
| **Apache Velocity** | 2.3 | 模板引擎，负责代码模板渲染 |
| **Knife4j** | 4.5.0 | API 文档增强工具，基于 OpenAPI 3.0 |

---

## 🔧 三方库深度解析

### 1️⃣ Apache Velocity — 灵活的模板引擎

[Apache Velocity](https://velocity.apache.org/) 是一款老牌而强大的 Java 模板引擎，最初由 Apache 基金会维护。它的设计哲学是 **MVC 分离**：将业务逻辑与展示层完全解耦。

#### 为什么选择 Velocity？

- **语法简洁直观**：使用 `#{变量}` 和 `#foreach` 等指令，学习成本低
- **高性能**：模板编译后缓存，渲染速度极快
- **高度可定制**：支持自定义指令、资源加载器等扩展机制
- **成熟稳定**：历经多年生产验证，生态完善

#### 本项目中的实战应用

我们在 [`VelocityTemplateService`](src/main/java/com/example/codegenerator/service/VelocityTemplateService.java) 中对 Velocity 进行了封装：

```java
// 初始化引擎配置
velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADERS, "classpath");
velocityEngine.setProperty("resource.loader.classpath.class", ClasspathResourceLoader.class.getName());
```

关键配置说明：
- **ResourceLoader**：使用 ClasspathResourceLoader 从 classpath 加载模板
- **RuntimeConstants**：Velocity 提供的常量配置类，避免硬编码字符串

#### 模板示例：生成 POJO 类

查看我们的 [`java-class.vm`](src/main/resources/templates/java-class.vm) 模板：

```velocity
package ${packageName};

public class ${className} {
#foreach($field in $fields)
private ${field.type} ${field.name};
#end

#foreach($field in $fields)
public ${field.type} get${field.name.substring(0,1).toUpperCase()}${field.name.substring(1)}() {
    return this.${field.name};
}
#end
}
```

**Velocity 语法亮点**：
- `${variable}`：变量插值
- `#foreach(item in list)`：循环遍历
- `${field.name.substring(0,1).toUpperCase()}`：支持方法链调用
- `$foreach.count`：内置循环计数器

#### 另一个模板：生成 Controller

[`controller.vm`](src/main/resources/templates/controller.vm) 展示了更复杂的场景——自动生成 RESTful API 的 CRUD 结构：

```velocity
@RestController
@RequestMapping("/api/${entityNameLower}")
public class ${className} {
    @GetMapping
    public List<${entityName}> getAll() { ... }

    @PostMapping
    public ${entityName} create(@RequestBody ${entityName} ${entityNameLower}) { ... }
}
```

通过传入不同的 `variables` Map，同一套模板可以生成无数种变体！

---

### 2️⃣ Knife4j — 让 API 文档"说话"

[Knife4j](https://doc.xiaominfo.com/) 是基于 OpenAPI 规范（前身是 Swagger）的 **API 文档增强解决方案**，专为国内开发者优化。

#### 核心优势

- **UI 更美观**：相比原生 Swagger UI，界面更符合中文开发者习惯
- **功能更丰富**：支持离线文档、接口调试、全局参数设置等
- **集成简单**：Spring Boot Starter 一键引入，零配置启动
- **活跃维护**：版本迭代快，社区响应及时

#### 集成方式（本项目）

在 [pom.xml](pom.xml) 中添加依赖：

```xml
<dependency>
    <groupId>com.github.xiaoymin</groupId>
    <artifactId>knife4j-openapi3-jakarta-spring-boot-starter</artifactId>
    <version>4.5.0</version>
</dependency>
```

注意 artifactId 中的 `jakarta` 关键字——这是适配 **Spring Boot 3.x**（使用 Jakarta EE 9+）的版本。

#### 访问文档

启动项目后，访问：
```
http://localhost:8080/doc.html
```

你将看到自动生成的交互式 API 文档，可以直接在页面上测试接口。

---

## 🎯 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+

### 启动步骤

```bash
# 克隆项目
git clone <repository-url>

# 进入目录
cd spring-velocity

# 构建并运行
mvn spring-boot:run
```

### 调用示例

项目启动后，调用 POST 接口生成代码：

```bash
curl -X POST http://localhost:8080/api/code/generate \
  -H "Content-Type: application/json" \
  -d '{
    "templateName": "java-class.vm",
    "variables": {
      "packageName": "com.example.model",
      "className": "User",
      "fields": [
        {"name": "id", "type": "Long"},
        {"name": "username", "type": "String"},
        {"name": "email", "type": "String"}
      ]
    }
  }'
```

**返回结果**：一段完整的、格式规范的 Java POJO 代码！

---

## 📐 项目结构

```
spring-velocity/
├── src/main/java/
│   └── com/example/codegenerator/
│       ├── controller/
│       │   └── CodeGenerationController.java    # REST API 入口
│       ├── dto/
│       │   └── CodeGenerationRequest.java       # 请求参数封装
│       ├── service/
│       │   └── VelocityTemplateService.java     # Velocity 引擎封装
│       └── SpringCodeGeneratorApplication.java  # 启动类
├── src/main/resources/
│   ├── templates/                               # Velocity 模板文件
│   │   ├── controller.vm                        # Controller 模板
│   │   └── java-class.vm                        # POJO 模板
│   └── application.properties                   # 配置文件
└── pom.xml
```

---

## 💡 扩展思路

1. **新增模板**：在 `templates/` 目录下添加 `.vm` 文件即可扩展
2. **自定义指令**：实现 Velocity 的 `Directive` 接口，支持更复杂的逻辑
3. **多引擎切换**：对比 FreeMarker、Thymeleaf 等模板引擎的优劣
4. **持久化存储**：将生成的代码写入文件系统或数据库

---

## 📚 相关资源

- [Apache Velocity 官方文档](https://velocity.apache.org/engine/devel/user-guide.html)
- [Knife4j 官方文档](https://doc.xiaominfo.com/)
- [OpenAPI 3.0 规范](https://spec.openapis.org/oas/latest.html)
- [Spring Boot 3.x 迁移指南](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-3.0-Migration-Guide)

---
