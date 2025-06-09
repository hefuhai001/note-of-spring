package com.example.springboot.controller;

import com.example.springboot.utils.uploadUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description
 * @Author XiaoHe
 * @CreateTime 2023/5/4 17:27
 */
@RestController
public class UploadController {

    @Operation(summary = "上传图片到本地")
    @PostMapping("/upload")
    public String upload(MultipartFile file) {
        if (file.isEmpty()) {
            return "图片为空";
        }
        String originalFilename = file.getOriginalFilename();
        String fileNamePrefix = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String fileNameSuffix = "." + originalFilename.split("\\.")[1];
        String fileName = fileNamePrefix + fileNameSuffix;
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        String pre = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath() +
                "\\src\\main\\resources\\static\\images\\";
        String path = pre + fileName;
        try {
            file.transferTo(new File(path));
            return path;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "图片上传失败";
    }

    @Operation(summary = "上传图片到云端")
    @PostMapping("/upImg")
    public String upImg(MultipartFile file) throws IOException {
        return uploadUtil.uploadImage(file);
    }

}
