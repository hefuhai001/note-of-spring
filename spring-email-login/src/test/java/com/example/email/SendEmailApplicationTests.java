package com.example.email;

import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Date;

@SpringBootTest
class SendEmailApplicationTests {

    // 这个是 mail 依赖提供给我们的发送邮件的接口
    @Autowired
    private JavaMailSender mailSender;

    // 获取发件人邮箱
    @Value("${spring.mail.username}")
    private String sender;

    // 获取发件人昵称
    @Value("${spring.mail.nickname}")
    private String nickname;

    @Test
    void testSendMail() {
        String email = "2109664977@qq.com";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(nickname + '<' + sender + '>');
        message.setTo(email);
        message.setSubject("邮箱登录验证码");
        // 设置邮件抄送人，可以有多个抄送人
        //message.setCc("***qq.com");
        // 设置隐秘抄送人，可以有多个
        //message.setBcc("***@qq.com");
        // 设置邮件发送日期
        message.setSentDate(new Date());

        // 使用 hutool-all 生成 6 位随机数验证码
        String code = RandomUtil.randomNumbers(6);

        String content = "【验证码】您的验证码为：" + code + " 。 验证码五分钟内有效，逾期作废。";

        message.setText(content);

        mailSender.send(message);
    }

}
