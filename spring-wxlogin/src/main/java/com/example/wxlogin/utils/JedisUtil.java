package com.example.wxlogin.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {
    // Jedis连接池
    private static final JedisPool jedisPool;

    // 初始化Jedis连接池
    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(100); // 最大连接数
        poolConfig.setMaxIdle(10); // 最大空闲连接数
        poolConfig.setMinIdle(2); // 最小空闲连接数
        poolConfig.setTestOnBorrow(true); // 在获取连接时检查连接是否有效
        poolConfig.setTestOnReturn(true); // 在返回连接时检查连接是否有效
        poolConfig.setTestWhileIdle(true); // 在空闲时检查连接是否有效

        // 创建Jedis连接池
        jedisPool = new JedisPool(poolConfig, "localhost", 6379, 10000);
    }

    // 获取Jedis连接
    private static Jedis getJedis() {
        return jedisPool.getResource();
    }

    // 关闭Jedis连接
    private static void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    // 设置字符串键值对
    public static void setStr(String key, String value) {
        Jedis jedis = getJedis();
        try {
            jedis.set(key, value);
        } finally {
            closeJedis(jedis);
        }
    }

    // 设置字符串键值对并设置过期时间（秒）
    public static void setStr(String key, String value, int seconds) {
        Jedis jedis = getJedis();
        try {
            jedis.setex(key, seconds, value);
        } finally {
            closeJedis(jedis);
        }
    }

    // 获取字符串键值对
    public static String getStr(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.get(key);
        } finally {
            closeJedis(jedis);
        }
    }

    // 删除键值对
    public static void delStr(String key) {
        Jedis jedis = getJedis();
        try {
            jedis.del(key);
        } finally {
            closeJedis(jedis);
        }
    }

    // 检查键是否存在
    public static boolean exists(String key) {
        Jedis jedis = getJedis();
        try {
            return jedis.exists(key);
        } finally {
            closeJedis(jedis);
        }
    }
}
