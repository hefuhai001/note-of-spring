package com.hfh.order.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
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
public class StockInitRunner implements CommandLineRunner {

    @Autowired
    StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) {
        redisTemplate.opsForValue().set("stock:sku:1001", "100"); // 100 件
    }

}
