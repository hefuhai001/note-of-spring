package com.hfh.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * <p>
 * Class description goes here.
 * </p>
 *
 * @author 何福海
 * @version 1.0
 * @since 2025/7/22
 */
@Service
public class SecKillService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisScript<Long> stockScript;

    public boolean deductStock(String sku, int num) {
        Long left = redisTemplate.execute(
                stockScript,
                Collections.singletonList("stock:sku:" + sku),
                String.valueOf(num));
        return left != null && left >= 0;
    }
}
