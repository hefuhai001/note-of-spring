package com.hfh.order.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p>
 * Class description goes here.
 * </p>
 *
 * @author 何福海
 * @version 1.0
 * @since 2025/7/22
 */
@Component
public class OrderConsumer {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Scheduled(fixedDelay = 100)
    public void consume() {
        String data = redisTemplate.opsForList().rightPop("order:queue");
        if (data != null) {
            // 解析并写数据库，此处可用乐观锁或数据库行锁再校验一次库存
            // ...
        }
    }
}
