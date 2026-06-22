package com.hfh.api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.hfh.api.entity.ProductOrder;
import com.hfh.api.mapper.ProductOrderMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 商品订单布隆过滤器
 * <p>
 * 用于快速判断订单号是否存在，防止缓存穿透。
 * 启动时同时预加载 Redis 缓存。
 * <p>
 * 布隆过滤器特性：
 * - 若返回 false，订单号一定不存在
 * - 若返回 true，订单号可能存在（有误判率）
 */
@Slf4j
@Component
public class ProductOrderBloomFilter {

    private static final int EXPECTED_INSERTIONS = 1_000_000;
    private static final double FPP = 0.01;
    private static final String CACHE_KEY_PREFIX = "order:";
    private static final long CACHE_TTL_MINUTES = 30;

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();

    private final ProductOrderMapper productOrderMapper;
    private final StringRedisTemplate redisTemplate;

    private BloomFilter<String> bloomFilter;

    public ProductOrderBloomFilter(ProductOrderMapper productOrderMapper,
                                   StringRedisTemplate redisTemplate) {
        this.productOrderMapper = productOrderMapper;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 应用启动时，从数据库加载所有订单：
     * 1. 初始化布隆过滤器（内存）
     * 2. 预加载 Redis 缓存
     */
    @PostConstruct
    public void init() {
        log.info("开始初始化布隆过滤器...");

        bloomFilter = BloomFilter.create(
                Funnels.stringFunnel(StandardCharsets.UTF_8),
                EXPECTED_INSERTIONS,
                FPP
        );

        // 加载所有有效订单
        List<ProductOrder> orders = productOrderMapper.selectAllOrders();

        // 第一层：写入布隆过滤器
        for (ProductOrder order : orders) {
            bloomFilter.put(order.getOrderNo());
        }
        log.info("[布隆过滤器] 初始化完成，已加载 {} 条订单号", orders.size());

        // 第二层：预加载 Redis 缓存
        preloadRedis(orders);
    }

    /**
     * 将订单数据批量预加载到 Redis
     */
    private void preloadRedis(List<ProductOrder> orders) {
        log.info("[Redis] 开始预加载缓存...");
        int successCount = 0;
        for (ProductOrder order : orders) {
            try {
                String key = CACHE_KEY_PREFIX + order.getOrderNo();
                String json = objectMapper.writeValueAsString(order);
                redisTemplate.opsForValue().set(key, json, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
                successCount++;
            } catch (JsonProcessingException e) {
                log.warn("[Redis] 序列化失败，跳过订单：{}", order.getOrderNo());
            }
        }
        log.info("[Redis] 预加载完成，成功写入 {}/{} 条", successCount, orders.size());
    }

    public boolean mightContain(String orderNo) {
        return bloomFilter.mightContain(orderNo);
    }

    public void add(String orderNo) {
        bloomFilter.put(orderNo);
    }

    public long approximateElementCount() {
        return bloomFilter.approximateElementCount();
    }
}
