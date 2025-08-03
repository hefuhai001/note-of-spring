package com.example.demo.base.service;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

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

    public String upload(MultipartFile file) {
        try {
            minio.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(file.getOriginalFilename())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
            // **返回可直链访问的 URL**
            return "http://localhost:9000/" + bucket + "/" + file.getOriginalFilename();
        } catch (Exception e) {
            throw new RuntimeException("上传失败", e);
        }
    }

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
