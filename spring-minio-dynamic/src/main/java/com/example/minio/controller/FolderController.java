package com.example.minio.controller;

import com.example.minio.util.MinioUtil;
import io.minio.Result;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
@RequestMapping("/folder")
public class FolderController {

    @Autowired
    private MinioUtil minioUtil;

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
    @GetMapping("/listFolder")
    public List<String> listFolder(@RequestParam String bucketName, @RequestParam(required = false) String prefix) {
        try {
            List<String> list = new ArrayList<>();
            for (Result<Item> itemResult : minioUtil.listObjects(bucketName, prefix)) {
                String objectName = itemResult.get().objectName();
                boolean isDir = objectName.endsWith("/");
                // 对于文件夹，直接使用原名称（已经以/结尾），对于文件，直接使用原名称
                list.add(objectName);
            }
            return list;
        } catch (Exception e) {
            return List.of("列出对象失败：" + e.getMessage());
        }
    }

    // 删除文件夹
    @DeleteMapping("/deleteFolder")
    public String deleteFolder(@RequestParam String bucketName, @RequestParam String folderName) {
        try {
            minioUtil.removeFolder(bucketName, folderName);
            return "文件夹删除成功";
        } catch (Exception e) {
            return "文件夹删除失败：" + e.getMessage();
        }
    }

    // 修改文件夹名称
    @PutMapping("/renameFolder")
    public String renameFolder(@RequestParam String bucketName, @RequestParam String oldFolderName, @RequestParam String newFolderName) {
        try {
            minioUtil.renameFolder(bucketName, oldFolderName, newFolderName);
            return "文件夹重命名成功";
        } catch (Exception e) {
            return "文件夹重命名失败：" + e.getMessage();
        }
    }

}
