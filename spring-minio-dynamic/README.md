# Spring Boot 3 + MinIO 动态文件存储

> 基于 MinIO 8.5.7 的 Spring Boot 3.2.4 文件管理解决方案，支持存储桶/文件夹/文件的完整 CRUD 操作

## 技术栈

| 组件 | 版本 |
|------|------|
| Java | 17 |
| Spring Boot | 3.2.4 |
| MinIO SDK | 8.5.7 |
| Knife4j (API文档) | 4.5.0 |

## 快速开始

### 1. 配置 MinIO 连接信息

```yaml
# application.yml
minio:
  endpoint: http://localhost:9000    # MinIO 服务地址
  accessKey: your-access-key         # 访问密钥
  secretKey: your-secret-key         # 秘密密钥
  bucket-name: default-bucket        # 默认存储桶

spring:
  servlet:
    multipart:
      max-file-size: 30MB            # 单文件最大限制
      max-request-size: 30MB         # 请求最大限制
```

### 2. 启动服务 & 访问文档

启动后访问 Knife4j 接口文档：`http://localhost:8080/doc.html`

## API 接口

### 存储桶管理 `/bucket`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/createBucket?bucketName=xxx` | 创建存储桶 |
| GET | `/listBuckets` | 获取所有存储桶列表 |
| DELETE | `/deleteBucket?bucketName=xxx` | 删除存储桶 |

### 文件夹管理 `/folder`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/createFolder?bucketName=xxx&folderName=xxx` | 创建文件夹 |
| GET | `/listFolder?bucketName=xxx&prefix=xxx` | 列出文件夹内容 |
| DELETE | `/deleteFolder?bucketName=xxx&folderName=xxx` | 删除文件夹（递归） |
| PUT | `/renameFolder?bucketName=xxx&oldFolderName=xxx&newFolderName=xxx` | 重命名文件夹 |

### 文件管理 `/file`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/upload?bucketName=xxx` (MultipartFile) | 上传文件 |
| GET | `/url?bucketName=xxx&fileName=xxx` | 获取临时访问链接（1小时有效） |
| GET | `/download?bucketName=xxx&fileName=xxx` | 下载文件 |
| DELETE | `/delete?bucketName=xxx&fileName=xxx` | 删除文件 |

## 核心代码说明

### MinIO 客户端动态配置

[MinioConfig.java](src/main/java/com/example/hfh/config/MinioConfig.java) - 通过 `@ConfigurationProperties` 绑定配置：

```java
@Configuration
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {
    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    // getters & setters...
}
```

[MinioConfiguration.java](src/main/java/com/example/hfh/config/MinioConfiguration.java) - 构建 `MinioClient` Bean：

```java
@Bean
public MinioClient minioClient() {
    return MinioClient.builder()
        .endpoint(minioConfig.getEndpoint())
        .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())
        .build();
}
```

### 工具类封装

[MinioUtil.java](src/main/java/com/example/hfh/util/MinioUtil.java) 封装了常用操作：

- **自动创建存储桶**: 上传文件时若存储桶不存在会自动创建
- **文件夹模拟**: MinIO 本身是扁平结构，通过 `objectName` 以 `/` 结尾来模拟文件夹
- **预签名 URL**: `getFileUrl()` 返回 1 小时有效的临时访问链接
- **文件夹重命名**: 先复制到新路径，再删除旧路径（MinIO 不支持原子重命名）

## 使用示例

### 上传文件

```bash
curl -X POST "http://localhost:8080/file/upload?bucketName=my-bucket" \
  -F "file=@test.jpg"
```

### 获取文件访问链接

```bash
curl "http://localhost:8080/file/url?bucketName=my-bucket&fileName=test.jpg"
# 返回: http://localhost:9000/my-bucket/test.jpg?X-Amz-Signature=xxx...
```

### 创建文件夹并上传

```bash
# 1. 创建文件夹
curl -X POST "http://localhost:8080/folder/createFolder?bucketName=my-bucket&folderName=images/"

# 2. 上传到指定文件夹（objectName 包含路径）
# 注意：当前 API 上传时 fileName 需要包含完整路径，如 "images/photo.jpg"
```

## 项目结构

```
src/main/java/com/example/minio/
├── config/
│   ├── MinioConfig.java           # 配置属性类
│   └── MinioConfiguration.java     # MinIO Client Bean 配置
├── controller/
│   ├── BucketController.java       # 存储桶接口
│   ├── FileController.java         # 文件接口
│   └── FolderController.java       # 文件夹接口
├── exception/
│   └── GlobalExceptionHandler.java # 全局异常处理
└── util/
    └── MinioUtil.java              # MinIO 操作工具类
```

## 常见问题

**Q: 为什么文件夹操作比较特殊？**
A: MinIO 是对象存储，没有真正的目录树。文件夹通过在 objectName 末尾加 `/` 模拟，删除文件夹需要递归删除所有对象。

**Q: 如何修改文件访问链接的有效期？**
A: 在 [MinioUtil.java#L89](src/main/java/com/example/hfh/util/MinioUtil.java#L89) 的 `getFileUrl()` 方法中修改 `.expiry()` 参数。

**Q: 支持大文件上传吗？**
A: 当前默认限制 30MB，可在 `application.yml` 中调整 `max-file-size`。如需分片上传需自行实现。
