package com.example.captcha.controller;

import com.example.captcha.utils.VerifyCodeUtils;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

public class CommonController {

    @Operation(summary = "生成验证码")
    @GetMapping("/generateImageCode")
    public void generateImageCode(HttpSession session, HttpServletResponse response) throws IOException {
        //随机生成四位随机数
        String code = VerifyCodeUtils.generateVerifyCode(4);
        //保存到session域中
        session.setAttribute("code", code);
        //根据随机数生成图片，reqponse响应图片
        response.setContentType("image/png");
        ServletOutputStream os = response.getOutputStream();
        VerifyCodeUtils.outputImage(130, 60, os, code);
    }
}
