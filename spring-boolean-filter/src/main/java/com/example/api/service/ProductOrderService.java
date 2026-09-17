package com.example.api.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.api.entity.ProductOrder;
import com.example.api.mapper.ProductOrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ProductOrderService extends ServiceImpl<ProductOrderMapper, ProductOrder> {

    private static final String CACHE_KEY_PREFIX = "order:";
    private static final long CACHE_TTL_MINUTES = 30;
    private static final long NULL_CACHE_TTL_MINUTES = 5;

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();

    private final ProductOrderBloomFilter bloomFilter;
    private final StringRedisTemplate redisTemplate;

    public ProductOrderService(ProductOrderBloomFilter bloomFilter,
                               StringRedisTemplate redisTemplate) {
        this.bloomFilter = bloomFilter;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据订单号查询订单（三级缓存：布隆过滤器 → Redis → MySQL）
     * <p>
     * 流程：
     * 1. 布隆过滤器判断，一定不存在则直接返回 null
     * 2. 查询 Redis 缓存，命中则直接返回
     * 3. 未命中则查询数据库，结果回填 Redis
     *
     * @param orderNo 订单号
     * @return 订单信息，不存在返回 null
     */
    public ProductOrder getByOrderNo(String orderNo) {
        // 第一层：布隆过滤器（内存）
        if (!bloomFilter.mightContain(orderNo)) {
            log.info("[布隆过滤器] 拦截：{} 一定不存在", orderNo);
            return null;
        }

        // 第二层：Redis 缓存
        String cacheKey = CACHE_KEY_PREFIX + orderNo;
        String cachedJson = redisTemplate.opsForValue().get(cacheKey);
        if (cachedJson != null) {
            // 空值缓存（防穿透标记）
            if ("NULL".equals(cachedJson)) {
                log.info("[Redis] 命中空值缓存：{}", orderNo);
                return null;
            }
            try {
                ProductOrder cached = objectMapper.readValue(cachedJson, ProductOrder.class);
                log.info("[Redis] 命中缓存：{}", orderNo);
                return cached;
            } catch (JsonProcessingException e) {
                log.warn("[Redis] 反序列化失败，删除脏数据：{}", orderNo);
                redisTemplate.delete(cacheKey);
            }
        }

        // 第三层：数据库
        log.info("[MySQL] 缓存未命中，查询数据库：{}", orderNo);
        ProductOrder order = getOne(new LambdaQueryWrapper<ProductOrder>()
                .eq(ProductOrder::getOrderNo, orderNo));

        // 回填 Redis
        if (order != null) {
            try {
                String json = objectMapper.writeValueAsString(order);
                redisTemplate.opsForValue().set(cacheKey, json, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
                log.info("[Redis] 回填缓存成功：{}", orderNo);
            } catch (JsonProcessingException e) {
                log.warn("[Redis] 序列化失败，跳过缓存写入：{}", e.getMessage());
            }
        } else {
            // 写入空值防止穿透
            redisTemplate.opsForValue().set(cacheKey, "NULL", NULL_CACHE_TTL_MINUTES, TimeUnit.MINUTES);
            log.info("[Redis] 写入空值防穿透：{}", orderNo);
        }

        return order;
    }

    /**
     * 创建订单（同步更新布隆过滤器 + Redis 缓存）
     *
     * @param order 订单信息
     * @return 是否创建成功
     */
    public boolean createOrder(ProductOrder order) {
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        // 写入数据库
        boolean saved = save(order);

        if (saved) {
            // 更新布隆过滤器
            bloomFilter.add(order.getOrderNo());
            log.info("[布隆过滤器] 新增：{}", order.getOrderNo());

            // 更新 Redis 缓存
            String cacheKey = CACHE_KEY_PREFIX + order.getOrderNo();
            try {
                String json = objectMapper.writeValueAsString(order);
                redisTemplate.opsForValue().set(cacheKey, json, CACHE_TTL_MINUTES, TimeUnit.MINUTES);
                log.info("[Redis] 写入新订单缓存：{}", order.getOrderNo());
            } catch (JsonProcessingException e) {
                log.warn("[Redis] 序列化失败：{}", e.getMessage());
            }
        }

        return saved;
    }
}
