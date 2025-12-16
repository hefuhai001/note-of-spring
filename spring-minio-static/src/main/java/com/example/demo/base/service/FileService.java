package com.example.demo.base.service;

import com.example.demo.FileUploadResp;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * Class description goes here.
 * </p>
 *
 * @author 何福海
 * @version 1.0
 * @since 2025/8/1
 */
@Service
@RequiredArgsConstructor
public class FileService {

    private final MinioClient minio;

    @Value("${minio.bucket-name}")
    private String bucket;

    public FileUploadResp upload(MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String extension = StringUtils.getFilenameExtension(originalFilename);
            String uniqueName = UUID.randomUUID() + (extension != null ? "." + extension : "");

            minio.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(uniqueName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());

            // 1. 永久路径（自己拼，适合后台拼接用）
            String permanentUrl = String.format("/minio/%s/%s", bucket, uniqueName);

            // 2. 临时签名外链（给前端用）
            String presignedUrl = minio.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(uniqueName)
                            .expiry(7, TimeUnit.DAYS)
                            .build());

            return new FileUploadResp(permanentUrl, presignedUrl);
        } catch (Exception e) {
            throw new RuntimeException("上传失败", e);
        }
    }

    // public String upload(MultipartFile file) {
    //     try {
    //         minio.putObject(
    //                 PutObjectArgs.builder()
    //                         .bucket(bucket)
    //                         .object(file.getOriginalFilename())
    //                         .stream(file.getInputStream(), file.getSize(), -1)
    //                         .contentType(file.getContentType())
    //                         .build());
    //         // **返回可直链访问的 URL**
    //         return "http://localhost:9000/" + bucket + "/" + file.getOriginalFilename();
    //     } catch (Exception e) {
    //         throw new RuntimeException("上传失败", e);
    //     }
    // }

    public InputStreamResource download(String fileName) {
        try {
            InputStream stream = minio.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .build());
            return new InputStreamResource(stream);
        } catch (Exception e) {
            throw new RuntimeException("下载失败", e);
        }
    }
}
