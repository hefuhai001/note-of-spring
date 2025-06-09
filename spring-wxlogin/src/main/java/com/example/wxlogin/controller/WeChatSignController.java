package com.example.wxlogin.controller;

import com.example.wxlogin.utils.JedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("/sign")
public class WeChatSignController {

    private static final String TOKEN = "xiaohelikesleep"; // 你的Token

    @RequestMapping("/wx")
    public void sign(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String method = request.getMethod();
        if ("GET".equals(method)) {
            // 验证签名
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");
            if (validateSignature(signature, timestamp, nonce, TOKEN)) {
                response.getWriter().print(echostr);
            }
        } else {
            WxMpXmlMessage msg = WxMpXmlMessage.fromXml(request.getInputStream());
            String fromUserName = msg.getFromUser();
            String event = msg.getEvent();

            log.info("msg:{}", msg);
            log.info("event:{}", event);
            log.info("fromUserName:{}", fromUserName);

            if ("subscribe".equals(event)) {
                // 用户扫码关注
                String sceneStr = msg.getEventKey().replace("qrscene_", "");
                JedisUtil.setStr(sceneStr, fromUserName, 3600);
            } else if ("SCAN".equals(event)) {
                // 用户已关注，再次扫码
                String sceneStr = msg.getEventKey();
                JedisUtil.setStr(sceneStr, fromUserName, 3600);
            }
        }
    }

    /**
     * 验证微信回调的签名
     *
     * @param signature 微信回调的签名
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param token     在微信公众平台设置的令牌（Token）
     * @return 验证结果
     */
    public static boolean validateSignature(String signature, String timestamp, String nonce, String token) {
        // 将token、timestamp、nonce三个参数进行字典序排序
        String[] arr = new String[]{token, timestamp, nonce};
        Arrays.sort(arr);

        // 将排序后的三个参数字符串拼接成一个字符串进行sha1加密
        StringBuilder sb = new StringBuilder();
        for (String s : arr) {
            sb.append(s);
        }
        String temp = sb.toString();
        String sha1 = DigestUtils.sha1Hex(temp);

        // 验证sha1加密后的字符串是否与微信回调的签名一致
        return sha1.equals(signature);
    }
}
