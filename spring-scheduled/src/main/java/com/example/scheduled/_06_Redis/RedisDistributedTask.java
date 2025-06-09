package com.example.scheduled._06_Redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
public class RedisDistributedTask {
    private final RedisTemplate<String, String> redisTemplate;

    public RedisDistributedTask(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Scheduled(fixedRate = 5000)
    public void executeTask() {
        String lockKey = "distributedTaskLock";
        String lockValue = String.valueOf(System.currentTimeMillis());
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(lockKey, lockValue, 5, TimeUnit.SECONDS);
        if (lock != null && lock) {
            System.out.println("分布式任务执行：" + new Date());
        }
    }
}
