package com.example.minio.controller;

import com.example.minio.util.MinioUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private MinioUtil minioUtil;

    // 上传文件
    @PostMapping("/upload")
    public String uploadFile(@RequestParam String bucketName, @RequestParam MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String contentType = file.getContentType();
            InputStream inputStream = file.getInputStream();
            minioUtil.uploadFile(bucketName, fileName, inputStream, contentType);
            return "文件上传成功";
        } catch (Exception e) {
            return "文件上传失败：" + e.getMessage();
        }
    }

    // 获取文件外链
    @GetMapping("/url")
    public String getPresignedObjectUrl(@RequestParam String bucketName, @RequestParam String fileName) {
        try {
            return minioUtil.getFileUrl(bucketName, fileName);
        } catch (Exception e) {
            return "获取文件外链失败：" + e.getMessage();
        }
    }

    // 下载文件
    @GetMapping("/download")
    public void downloadFile(@RequestParam String bucketName, @RequestParam String fileName, HttpServletResponse response) {
        try {
            InputStream fileInputStream = minioUtil.getObject(bucketName, fileName);
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("application/force-download");
            response.setCharacterEncoding("UTF-8");
            fileInputStream.transferTo(response.getOutputStream());
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    // 删除文件
    @DeleteMapping("/delete")
    public String deleteFile(@RequestParam String bucketName, @RequestParam String fileName) {
        try {
            minioUtil.removeFile(bucketName, fileName);
            return "文件删除成功";
        } catch (Exception e) {
            return "文件删除失败：" + e.getMessage();
        }
    }

}
