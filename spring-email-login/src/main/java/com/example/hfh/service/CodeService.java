package com.example.hfh.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class CodeService {

    // 获取发件人邮箱
    @Value("${spring.mail.username}")
    private String sender;

    // 获取发件人昵称
    @Value("${spring.mail.nickname}")
    private String nickname;

    public List<String> generateRandomNumbers() {
        List<String> randomNumbers = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            int num = random.nextInt(10); // 生成0到9之间的随机数
            randomNumbers.add(String.valueOf(num));
        }
        return randomNumbers;
    }


    public StringBuilder combineRandomNumbers(List<String> list) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String integer = list.get(i);
            String s = String.valueOf(integer);
            string.append(s);
        }
        return string;
    }


    public MimeMessageHelper getMimeMessageHelper(MimeMessage mimeMessage, String email) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("测试发送Thymeleaf模板Email");
        helper.setFrom(nickname + '<' + sender + '>');
        // helper.setFrom("18365404517@163.com");  // 设置邮件发送者，这个跟application.yml中设置的要一致
        helper.setTo(email);  // 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似
        // 设置邮件抄送人，可以有多个抄送人
        //message.setCc("***qq.com");
        // 设置隐秘抄送人，可以有多个
        // message.setBcc("***@qq.com");
        // 设置邮件发送日期
        helper.setSentDate(new Date());
        return helper;
    }


}
