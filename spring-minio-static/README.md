# Spring Boot + MinIO 文件服务

基于 **Spring Boot 3.2** 集成 MinIO 对象存储，实现文件上传/下载，附带 Feign 远程调用与 Knife4j 文档。

---

## 技术栈 & 第三方库

| 库 | 版本 | 用途 |
|---|---|---|
| [MinIO Java SDK](https://docs.min.io/docs/java-client-api-reference.html) | 8.5.7 | 对象存储客户端 |
| [Spring Cloud OpenFeign](https://spring.io/projects/spring-cloud-openfeign) | 4.1.0 | 声明式 HTTP 调用 |
| [Knife4j](https://doc.xiaominfo.com/) | 4.5.0 | OpenAPI 3 文档增强 |
| Lombok | - | 省掉 getter/setter |

---

## MinIO SDK 核心用法

### 1. 客户端初始化 & 自动建桶

```java
@Bean
public MinioClient minioClient() throws Exception {
    MinioClient client = MinioClient.builder()
            .endpoint(url)
            .credentials(accessKey, secretKey)
            .build();

    if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
        client.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
    }
    return client;
}
```

> 启动时自动检测 bucket 是否存在，不存在则创建。生产环境建议通过 MinIO 控制台或 lifecycle policy 管理桶策略。

### 2. 文件上传

```java
minio.putObject(
    PutObjectArgs.builder()
        .bucket(bucketName)
        .object(UUID + "." + extension)   // 用 UUID 防文件名冲突
        .stream(file.getInputStream(), file.getSize(), -1)
        .contentType(file.getContentType())
        .build());
```

关键点：
- `object` 参数就是存储路径，支持带 `/` 的目录结构如 `2026/05/xxx.png`
- `stream` 第三个参数传 `-1` 表示不知道确切大小，让 SDK 自行处理
- 返回两种 URL：
  - **permanentUrl**: 自己拼的相对路径 `/minio/{bucket}/{filename}`，存数据库用
  - **presignedUrl**: MinIO 签名的临时外链，默认 7 天过期，前端直接访问

### 3. 生成预签名 URL（核心）

```java
String url = minio.getPresignedObjectUrl(
    GetPresignedObjectUrlArgs.builder()
        .method(Method.GET)
        .bucket(bucketName)
        .object(objectName)
        .expiry(7, TimeUnit.DAYS)
        .build());
```

> 这是 MinIO 最常用的能力——私有桶不能直接访问，必须通过签名 URL 临时授权。过期后需重新生成。

### 4. 文件下载

```java
InputStream stream = minio.getObject(
    GetObjectArgs.builder()
        .bucket(bucketName)
        .object(fileName)
        .build());
```

返回 InputStream，配合 `ResponseEntity<InputStreamResource>` 实现流式下载，大文件不爆内存。

---

## OpenFeign 远程调用

```java
@FeignClient(name = "file-service", url = "http://localhost:8080")
public interface FileFeignClient {

    @PostMapping(value = "/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    String upload(@RequestPart("file") MultipartFile file);

    @GetMapping("/file/download/{fileName}")
    byte[] download(@PathVariable String fileName);
}
```

注意点：
- 文件上传必须显式指定 `consumes = MULTIPART_FORM_DATA_VALUE`，否则 Feign 序列化会出错
- 下载返回 `byte[]` 适合小文件，大文件场景建议用 Feign 的 Stream 方式

---

## 配置项

```properties
# MinIO 连接信息
minio.url=http://localhost:9000
minio.access-key=minioadmin
minio.secret-key=minioadmin
minio.bucket-name=files

# 文件大小限制
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

server.port=8081
```

---

## API 接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/file/upload` | 上传文件，返回 permanentUrl + presignedUrl |
| GET | `/file/download/{fileName}` | 流式下载文件 |

启动后访问 `http://localhost:8081/doc.html` 查看 Knife4j 文档界面。

---

## 项目结构

```
src/main/java/com/example/demo/
├── DemoApplication.java
├── FileUploadResp.java              # 上传响应 DTO
└── base/
    ├── config/MinioConfig.java      # MinIO 客户端配置
    ├── controller/FileController.java
    ├── service/FileService.java     # 核心业务逻辑
    └── mapper/FileFeignClient.java  # Feign 远程接口
```
