package com.example.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Tag(name="上传图片")
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
        assert originalFilename != null;
        String fileNameSuffix = "." + originalFilename.split("\\.")[1];
        String fileName = fileNamePrefix + fileNameSuffix;
        ApplicationHome applicationHome = new ApplicationHome(this.getClass());
        //String pre = applicationHome.getDir().getParentFile().getParentFile().getAbsolutePath() + "\\src\\main\\resources\\static\\images\\";
        String pre = applicationHome.getDir().getParentFile().getParentFile().getParentFile().getAbsolutePath() + "\\spring-ui\\src\\assets\\avatar\\";
        String path = pre + fileName;
        try {
            file.transferTo(new File(path));
            String replace = path.replace(applicationHome.getDir().getParentFile().getParentFile().getParentFile().getAbsolutePath() + "\\spring-ui\\src", "/src");
            return replace.replace("\\", "/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "图片上传失败";
    }
}
