package com.common.utils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author bestwishes0203
 * @since 2024-03-20
 */
@Tag(name = "验证码")
@RestController
public class GenerateImageCode {

    /**
     * @param session  会话
     * @param response 返回
     * @throws IOException 校验
     */
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
