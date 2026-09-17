package com.example.hfh.controller;

import com.example.hfh.service.CodeService;
import com.example.hfh.service.RedisService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController("/login")
public class LoginController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    /*
     * 填写邮箱获取验证码
     * */
    @GetMapping("/code")
    public String code(@RequestParam String email) throws MessagingException {
        List<String> list = codeService.generateRandomNumbers();
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = codeService.getMimeMessageHelper(mimeMessage, email);
        // 这里引入的是Template的Context
        Context context = new Context();
        // 设置模板中的变量
        context.setVariable("text", list);
        // 第一个参数为模板的名称
        String process = templateEngine.process("login-template.html", context);
        // 第二个参数true表示这是一个html文本
        helper.setText(process, true);
        mailSender.send(mimeMessage);
        StringBuilder stringBuilder = codeService.combineRandomNumbers(list);
        String string = stringBuilder.toString();
        //存入redis
        redisService.setWithExpire(email, string, 600, TimeUnit.SECONDS);
        return "验证码发送成功，10分钟内有效";
    }


    /*
     * 填写验证码登录
     * */
    @GetMapping("/login")
    public String login(@RequestParam String email, @RequestParam String code) {
        Object object = redisService.get(email);
        if (object == null) {
            return "验证码已过期，请重新获取";
        }
        if (object.equals(code)) {
            return "验证码正确";
        }
        return "验证码错误";
    }


}
