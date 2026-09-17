package com.example.hfh.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.hfh.annotation.JwtToken;
import com.example.hfh.resp.ApiResponse;
import com.example.hfh.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/common")
public class BaseController {

    @PostMapping("/login")
    public ApiResponse<?> login(@RequestParam String acc, @RequestParam String pwd) {
        if (Objects.equals(acc, "1") && Objects.equals(pwd, "1")) {
            HashMap<String, String> map = new HashMap<>();
            map.put("acc", acc);
            map.put("pwd", pwd);
            String token = JWTUtils.generateToken(map);
            return ApiResponse.success("登陆成功", token);
        }
        return ApiResponse.failure("登录失败");
    }

    @JwtToken
    @PostMapping("/test")
    public ApiResponse<?> test(HttpServletRequest httpServletRequest) {
        String token = httpServletRequest.getHeader("token");
        DecodedJWT decodedJWT = JWTUtils.resolveToken(token);
        Date expiresAt = decodedJWT.getExpiresAt();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeLag = expiresAt.getTime() - new Date().getTime();
        HashMap<String, String> map = new HashMap<>();
        map.put("Token生成时间", sdf.format(expiresAt));
        long day = timeLag / (24 * 60 * 60 * 1000); //天
        long hour = (timeLag / (60 * 60 * 1000) - day * 24); //小时
        long minute = ((timeLag / (60 * 1000)) - day * 24 * 60 - hour * 60); //分钟
        long s = (timeLag / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60); //1秒 = 1000毫秒
        map.put("Token剩余时间", day + "天 " + hour + "时 " + minute + "分 " + s + "秒");
        return ApiResponse.success("JWT 测试接口", map);
    }

}
