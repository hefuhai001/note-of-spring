package com.hfh.api.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class HealthCheckConfig {

    private final MysqlHealthCheck mysqlHealthCheck;
    private final RedisHealthCheck redisHealthCheck;

    public HealthCheckConfig(MysqlHealthCheck mysqlHealthCheck, RedisHealthCheck redisHealthCheck) {
        this.mysqlHealthCheck = mysqlHealthCheck;
        this.redisHealthCheck = redisHealthCheck;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        System.out.println("============================================");
        System.out.println("  Knife4j : http://localhost:8080/doc.html");
        System.out.println("============================================");
        mysqlHealthCheck.check();
        redisHealthCheck.check();
    }
}
