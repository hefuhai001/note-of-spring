package com.example.redis._demo;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

@RestController
public class test04 {

    @GetMapping("test04")
    public void test04(){

        /*
        * 为了减少频繁的创建和销毁jedis对象，提高了jedis的连接池，以提高连接效率。JedisPool
        * */

        //创建连接池的配置类
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMinIdle(5); //设置空闲的个数
        config.setMaxIdle(10);
        config.setMaxTotal(2000); //设置最多的数量
        config.setMaxWaitMillis(6000);//设置最大的等待时长
        config.setTestOnBorrow(true); //是否检验池子中的jedis对象可用
        //创建jedis连接池对象.格式 配置类 ip地址 端口号 超时时间 密码
        JedisPool jedisPool = new JedisPool(config, "127.0.0.1", 6379, 1000, "rootXH");

        //通过池子获取其中的一个连接 然后其他的操作和上面的一样
        Jedis jedis = jedisPool.getResource();

        Map<String, String> map1 = jedis.hgetAll("myhash");
        System.out.println("获取指定key对应的内容:" + map1);

    }
}
