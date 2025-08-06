package com.hfh.order.controller;

import com.hfh.order.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * Class description goes here.
 * </p>
 *
 * @author 何福海
 * @version 1.0
 * @since 2025/7/22
 */

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SecKillService secKillService;

    @PostMapping("/create")
    public String create(@RequestParam String sku,
                         @RequestParam int num) {
        if (!secKillService.deductStock(sku, num)) {
            return "库存不足";
        }
        // 生成订单号并放入 Redis 队列异步落库
        String orderId = STR."O\{System.currentTimeMillis()}";
        redisTemplate.opsForList().leftPush("order:queue",
                STR."\{orderId}:\{sku}:\{num}");
        return STR."下单成功，订单号：\{orderId}";
    }

}
