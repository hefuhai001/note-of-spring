# Spring File 文件管理系统

基于 Spring Boot 3.1.4 + MyBatis 的文件上传与管理项目，集成阿里云 OSS 对象存储。

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.1.4 | 基础框架 |
| MyBatis | 3.0.3 | ORM 持久层 |
| MySQL | - | 数据存储 |
| Thymeleaf | - | 页面模板 |

---

## 第三方库详解

### 1. Hutool 5.8.18

> 一站式 Java 工具集，替代手写工具类

**核心能力：**
- `IoUtil` — 流拷贝、读写，替代原生 `InputStream/OutputStream` 繁琐操作
- `FileUtil` — 文件创建、删除、移动、扩展名判断
- `StrUtil` — 字符串判空、截取、格式化（比 Apache Commons Lang 更简洁）
- `IdUtil` — UUID、雪花 ID 生成
- `DigestUtil` — MD5 / SHA 摘要计算

**本项目用法示例：**
```java
// 获取文件扩展名
String ext = FileUtil.extName(file.getOriginalFilename());

// 生成唯一文件名
String newFileName = IdUtil.fastSimpleUUID() + "." + ext;

// 计算文件 MD5（去重判断）
String md5 = DigestUtil.md5Hex(file.getInputStream());
```

**为什么选它而不选 Apache Commons：** API 设计更符合中文开发者直觉，方法链式调用友好，单个依赖覆盖 90% 场景。

---

### 2. Knife4j 4.1.0

> Swagger 的国产增强版，接口文档 UI 更好看

**核心能力：**
- 自动扫描 `@Tag` / `@Operation` 注解生成文档
- 在线调试接口（直接发请求）
- 支持 OpenAPI 3.0 规范
- 导出 Markdown / HTML 离线文档

**配置要点：**
```yaml
knife4j:
  enable: true
  setting:
    language: zh_cn
```

访问地址：`http://localhost:9090/doc.html`

**注解速查：**
```java
@Tag(name = "文件管理")
@RestController
@RequestMapping("/file")
public class FileController {

    @Operation(summary = "上传文件")
    @PostMapping("/upload")
    public Result upload(@RequestParam MultipartFile file) { ... }
}
```

**与 Swagger 区别：** Knife4j 本质是 Swagger UI 的皮肤增强，后端仍用 springdoc-openapi 解析，但前端界面更清晰，支持搜索、收藏、全局参数设置。

---

### 3. 阿里云 OSS SDK 3.16.1

> 对象存储，文件托管到云端

**核心流程：**
```java
// 1. 创建 Client（建议配为 Spring Bean）
OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

// 2. 上传
PutObjectRequest request = new PutObjectRequest(bucketName, objectName, inputStream);
ossClient.putObject(request);

// 3. 获取访问 URL
String url = "https://" + bucketName + "." + endpoint + "/" + objectName;

// 4. 关闭（必须）
ossClient.shutdown();
```

**关键概念：**
- **Bucket**：存储空间，类似"文件夹的根"
- **Object**：具体文件，key 为完整路径如 `uploads/2024/01/file.png`
- **Endpoint**：地域节点，如 `oss-cn-hangzhou.aliyuncs.com`
- **STS 临时凭证**：生产环境不要硬编码 AccessKey，用 STS 或 RAM 角色授权

**本项目中典型用法：** 用户上传 → 服务端接收 MultipartFile → 上传至 OSS → 返回 URL 存数据库。

---

### 4. Commons IO 2.11.0

> Apache 出品的 IO 操作增强库

**核心工具：**
- `FileUtils` — 文件复制、目录清理、按大小/时间筛选文件
- `IOUtils` — 流之间高效拷贝（自带 buffer 管理）
- `FilenameUtils` — 路径规范化、获取扩展名、拼接路径

**与 Hutool 的关系：** 功能有重叠，Hutool 的 `FileUtil` / `IoUtil` 已能覆盖大部分场景。Commons IO 的优势在于生态兼容性（很多框架底层依赖它）。本项目两者并存，按习惯选用即可。

---

### 5. Commons BeanUtils 1.9.4

> JavaBean 属性拷贝与操作

**核心用途：**
```java
// DTO → Entity 属性拷贝（忽略 null 值字段）
BeanUtils.copyProperties(entity, dto);

// 动态描述 Bean
PropertyUtils.getPropertyDescriptor(bean, "fieldName");
```

**注意：** 性能不如 MapStruct 或 Spring 的 `BeanUtils.copyProperties()`，大量数据场景慎用。适合配置读取、动态表单等低频场景。

**Spring 自带 vs Apache 版本：** Spring 的 `org.springframework.beans.BeanUtils` 不支持 Map ↔ Bean 转换，Apache 版支持但性能差。推荐优先用 Spring 原生，需要 Map 操作时再用 Apache。

---

### 6. Lombok

> 编译期代码生成，消灭样板代码

**常用注解：**

| 注解 | 效果 |
|------|------|
| `@Data` | getter + setter + toString + equals + hashCode |
| `@Builder` | 建造者模式 |
| `@NoArgsConstructor` / `@AllArgsConstructor` | 构造器 |
| `@Slf4j` | 日志对象注入 |

**IDE 配置要求：** IDEA 需安装 Lombok 插件并开启 Annotation Processing，否则编译报错。

---

## 项目结构

```
spring-file/
├── src/main/resources/
│   ├── application.yml          # 数据源 / OSS / Knife4j 配置
│   ├── mapper/                  # MyBatis XML 映射文件
│   └── templates/index.html     # 前端页面
├── pom.xml                      # 依赖管理
└── README.md
```

## 启动方式

```bash
# 确保 MySQL 已启动且创建了对应数据库
mvn spring-boot:run
```

访问地址：
- 前端页面：`http://localhost:9090`
- 接口文档：`http://localhost:9090/doc.html`
