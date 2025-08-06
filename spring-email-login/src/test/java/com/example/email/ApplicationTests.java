package com.example.email;

import com.example.email.service.RedisService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.*;

@SpringBootTest
class EmailApplicationTests {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private RedisService redisService;

    // 获取发件人邮箱
    @Value("${spring.mail.username}")
    private String sender;

    // 获取发件人昵称
    @Value("${spring.mail.nickname}")
    private String nickname;

    public static List<String> generateRandomNumbers() {
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

    @Test
    public void textRandom() {
        List<String> text = generateRandomNumbers();
        System.out.println(text);
        StringBuilder stringBuilder = combineRandomNumbers(text);
        System.out.println(stringBuilder);
    }

    @Test
    public void sendThymeleafMail() throws MessagingException {
        String email = "2109664977@qq.com";
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = getMimeMessageHelper(mimeMessage, email);
        // 这里引入的是Template的Context
        Context context = new Context();

        //List<String> text = Arrays.asList("1", "2", "3", "4", "5", "6");
        List<String> text = generateRandomNumbers();
        // 设置模板中的变量
        context.setVariable("text", text);

        // 第一个参数为模板的名称
        String process = templateEngine.process("login-template.html", context);
        // 第二个参数true表示这是一个html文本
        helper.setText(process, true);
        mailSender.send(mimeMessage);
    }

    private MimeMessageHelper getMimeMessageHelper(MimeMessage mimeMessage, String email) throws MessagingException {
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setSubject("测试发送Thymeleaf模板Email");
        helper.setFrom(nickname + '<' + sender + '>');
        //        helper.setFrom("18365404517@163.com");  // 设置邮件发送者，这个跟application.yml中设置的要一致
        helper.setTo(email);  // 设置邮件接收者，可以有多个接收者，中间用逗号隔开，以下类似
        // 设置邮件抄送人，可以有多个抄送人
        //message.setCc("***qq.com");
        // 设置隐秘抄送人，可以有多个
        //message.setBcc("***@qq.com");
        // 设置邮件发送日期
        helper.setSentDate(new Date());
        return helper;
    }


    @Test
    public void testRedis() {
        //redisService.set("key", "value");
        Object object = redisService.get("key");
        System.out.println(object);
        //比较地址
        System.out.println(object == "value");
        //比较值
        System.out.println(Objects.equals(object, "value"));
        String string = object.toString();
        System.out.println(Objects.equals(string, "value"));
    }


}
