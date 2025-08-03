package com.example.minio.controller;

import com.example.minio.util.MinioUtil;
import io.minio.Result;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/minio")
public class MinioController {

    @Autowired
    private MinioUtil minioUtil;

    // 创建存储桶
    @PostMapping("/createBucket")
    public String createBucket(@RequestParam String bucketName) {
        try {
            minioUtil.createBucket(bucketName);
            return "存储桶创建成功";
        } catch (Exception e) {
            return "存储桶创建失败：" + e.getMessage();
        }
    }

    // 获取所有存储桶列表
    @GetMapping("/listBuckets")
    public List<String> listBuckets() {
        try {
            return minioUtil.getAllBuckets().stream().map(Bucket::name).collect(Collectors.toList());
        } catch (Exception e) {
            return List.of("获取存储桶列表失败：" + e.getMessage());
        }
    }

    // 删除存储桶
    @DeleteMapping("/deleteBucket")
    public String deleteBucket(@RequestParam String bucketName) {
        try {
            minioUtil.removeBucket(bucketName);
            return "存储桶删除成功";
        } catch (Exception e) {
            return "存储桶删除失败：" + e.getMessage();
        }
    }

    // 创建文件夹
    @PostMapping("/createFolder")
    public String createFolder(@RequestParam String bucketName, @RequestParam String folderName) {
        try {
            minioUtil.createFolder(bucketName, folderName);
            return "文件夹创建成功";
        } catch (Exception e) {
            return "文件夹创建失败：" + e.getMessage();
        }
    }

    // 列出存储桶中的文件夹和文件
    @GetMapping("/listObjects")
    public List<String> listObjects(@RequestParam String bucketName, @RequestParam String prefix) {
        try {
            List<String> list = new ArrayList<>();
            for (Result<Item> itemResult : minioUtil.listObjects(bucketName, prefix)) {
                String objectName = itemResult.get().objectName();
                boolean isDir = objectName.endsWith("/");
                String apply = isDir ? objectName + "/" : objectName;
                list.add(apply);
            }
            return list;
        } catch (Exception e) {
            return List.of("列出对象失败：" + e.getMessage());
        }
    }

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