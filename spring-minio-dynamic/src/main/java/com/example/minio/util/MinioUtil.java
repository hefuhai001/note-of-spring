package com.example.minio.util;

import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;

    // 确保存储桶存在
    public void createBucket(String bucketName) throws Exception {
        if (!bucketExists(bucketName)) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    // 检查存储桶是否存在
    public boolean bucketExists(String bucketName) throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    // 获取所有存储桶列表
    public List<Bucket> getAllBuckets() throws Exception {
        return minioClient.listBuckets();
    }

    // 删除存储桶
    public void removeBucket(String bucketName) throws Exception {
        minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
    }

    // 模拟在存储桶中创建文件夹
    public void createFolder(String bucketName, String folderName) throws Exception {
        createBucket(bucketName);
        String folderPath = folderName.endsWith("/") ? folderName : folderName + "/";
        InputStream emptyStream = new ByteArrayInputStream(new byte[0]);
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(folderPath)
                        .stream(emptyStream, 0, 10485760)
                        .contentType("application/octet-stream")
                        .build()
        );
    }

    // 列出存储桶中的文件夹和文件
    public List<Result<Item>> listObjects(String bucketName, String prefix) throws Exception {
        return StreamSupport.stream(
                minioClient.listObjects(
                        ListObjectsArgs.builder()
                                .bucket(bucketName)
                                .prefix(prefix)
                                .recursive(false)
                                .build()
                ).spliterator(), false
        ).collect(Collectors.toList());
    }

    // 上传文件
    public void uploadFile(String bucketName, String objectName, InputStream inputStream, String contentType) throws Exception {
        createBucket(bucketName);
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(inputStream, -1, 10485760)
                        .contentType(contentType)
                        .build()
        );
    }

    // 获取文件访问 URL（临时链接，有效期 1 小时）
    public String getFileUrl(String bucketName, String objectName) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .method(Method.GET)
                        .expiry(1, java.util.concurrent.TimeUnit.HOURS)
                        .build()
        );
    }

    // 获取文件对象
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }

    // 删除文件
    public void removeFile(String bucketName, String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build()
        );
    }
}