package com.example.hfh.controller;

import com.example.hfh.util.MinioUtil;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
@RequestMapping("/bucket")
public class BucketController {

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


}
