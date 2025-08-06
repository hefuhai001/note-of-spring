package com.example.jwt.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    // 签名密钥
    private static final String SECRET = "!DEAR$";
    private static final int EXPIRE_TIME = 60 * 60;//自定义修改

    /**
     * 生成token 封装
     *
     * @param payload token携带的信息
     * @return token字符串
     */
    public static String generateToken(Map<String, String> payload) {
        // 指定token过期时间为7天
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, EXPIRE_TIME);
        JWTCreator.Builder builder = JWT.create();
        // 构建payload
        payload.forEach((k, v) -> builder.withClaim(k, v));
        // 指定过期时间和签名算法
        return builder.withExpiresAt(calendar.getTime()).sign(Algorithm.HMAC256(SECRET));
    }


    /**
     * 解析token 封装
     *
     * @param token token字符串
     * @return 解析后的token
     */
    public static DecodedJWT resolveToken(String token) {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        return jwtVerifier.verify(token);
    }


    /**
     * 生成token 基础用法
     *
     * @param name token携带的信息
     * @return token字符串
     */
    public String getToken(String name) {
        // 指定token过期时间为10秒
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, EXPIRE_TIME);
        String token = JWT.create()
                .withHeader(new HashMap<>())  // Header
                .withClaim("userName", name)  // Payload
                .withExpiresAt(calendar.getTime())  // 过期时间
                .sign(Algorithm.HMAC256(SECRET));  // 签名用的secret
        System.out.println(token);
        return token;
    }

    /**
     * 解析token 基础用法
     *
     * @param token token字符串
     * @return 解析后的token
     */
    public DecodedJWT decodeToken(String token) {
        // 创建解析对象，使用的算法和secret要与创建token时保持一致
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        // 解析指定的token
        DecodedJWT decodedJWT = jwtVerifier.verify(token);
        // 获取解析后的token中的 payload信息
        String userName = decodedJWT.getClaim("userName").asString();
        System.out.println(userName);
        // 输出超时时间
        System.out.println(decodedJWT.getExpiresAt());
        return decodedJWT;
    }


}
