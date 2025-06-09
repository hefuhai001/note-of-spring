package com.example.springoss.controller;

import com.example.springoss.utils.uploadUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/oss")
public class OssController {

    @PostMapping("/upImg")
    public String upImg(MultipartFile file) throws IOException {
        return uploadUtil.uploadImage(file);
    }

}
