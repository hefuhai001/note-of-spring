package com.example.redis._demo;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.Set;

@RestController
public class test01 {

    @GetMapping("test01")
    public void test01(){

        /*
        * 连接方式通过jar包直接连接
        * */
        //配置连接服务
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        //输入设置的数据库密码
        jedis.auth("rootXH");

        //获取所有的key
        Set<String> keys = jedis.keys("*");
        keys.forEach(System.out::println);

        jedis.set("k1", "v1");
        jedis.set("k2", "v2");
        jedis.set("k3", "v3");
        jedis.set("k4", "v4");
        jedis.set("k5", "v5");

        //删除指定的key
        Long c = jedis.del("k1", "k2", "k5");
        System.out.println("删除key的个数:" + c);

        //判断指定的key是否存在
        Boolean exists = jedis.exists("k2");
        System.out.println("判断key是否存在:" + exists);

        //关闭资源
        jedis.close();
    }
}
