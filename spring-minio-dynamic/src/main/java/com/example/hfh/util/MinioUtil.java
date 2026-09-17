package com.example.hfh.util;

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
        ListObjectsArgs.Builder builder = ListObjectsArgs.builder()
                .bucket(bucketName)
                .recursive(false);
        
        if (prefix != null && !prefix.isEmpty()) {
            builder.prefix(prefix);
        }
        
        return StreamSupport.stream(
                minioClient.listObjects(builder.build()).spliterator(), false
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

    // 删除文件夹（包括文件夹内的所有文件）
    public void removeFolder(String bucketName, String folderName) throws Exception {
        String folderPath = folderName.endsWith("/") ? folderName : folderName + "/";
        
        // 递归删除文件夹下的所有对象
        List<Result<Item>> objects = listObjects(bucketName, folderPath);
        for (Result<Item> itemResult : objects) {
            String objectName = itemResult.get().objectName();
            removeFile(bucketName, objectName);
        }
        
        // 删除文件夹本身
        removeFile(bucketName, folderPath);
    }

    // 修改文件夹名称（重命名文件夹）
    public void renameFolder(String bucketName, String oldFolderName, String newFolderName) throws Exception {
        String oldFolderPath = oldFolderName.endsWith("/") ? oldFolderName : oldFolderName + "/";
        String newFolderPath = newFolderName.endsWith("/") ? newFolderName : newFolderName + "/";
        
        // 列出旧文件夹下的所有对象
        List<Result<Item>> objects = listObjects(bucketName, oldFolderPath);
        
        // 创建新文件夹
        createFolder(bucketName, newFolderName);
        
        // 复制所有对象到新文件夹
        for (Result<Item> itemResult : objects) {
            String oldObjectName = itemResult.get().objectName();
            String newObjectName = oldObjectName.replace(oldFolderPath, newFolderPath);
            
            // 复制对象
            minioClient.copyObject(
                CopyObjectArgs.builder()
                    .bucket(bucketName)
                    .object(newObjectName)
                    .source(CopySource.builder().bucket(bucketName).object(oldObjectName).build())
                    .build()
            );
        }
        
        // 删除旧文件夹及其内容
        removeFolder(bucketName, oldFolderName);
    }
}