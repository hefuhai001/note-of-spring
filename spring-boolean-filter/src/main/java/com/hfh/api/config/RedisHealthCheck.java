package com.hfh.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisHealthCheck {

    private final StringRedisTemplate redisTemplate;

    public RedisHealthCheck(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void check() {
        try {
            String result = redisTemplate.getConnectionFactory().getConnection().ping();
            if ("PONG".equals(result)) {
                System.out.println("[Redis] success");
            } else {
                System.err.println("[Redis] ping : " + result);
            }
        } catch (Exception e) {
            System.err.println("[Redis] error: " + e.getMessage());
        }
    }
}
