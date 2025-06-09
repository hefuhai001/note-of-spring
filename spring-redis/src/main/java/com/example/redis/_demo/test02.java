package com.example.redis._demo;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

@RestController
public class test02 {

    @GetMapping("test02")
    public void test02(){

        /*
        * 连接方式通过jar包直接连接
        * */

        //配置连接服务
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        //输入设置的数据库密码
        jedis.auth("rootXH");
        //存放字符串数据
        jedis.set("k1", "1");
        jedis.set("k2", "v2");

        //获取指定key的内容
        String value1 = jedis.get("k1");
        System.out.println("k1对应的内容:" + value1);

        //如果指定的key存在则不存入，不存在则存入redis
        Long aLong = jedis.setnx("k8", "ldh");
        System.out.println("是否存入:" + aLong);

        //存入时设置过期时间
        String str = jedis.setex("k5", 30l, "v5");
        System.out.println("存入的内容:" + str);

        //用于点赞和收藏
        Long incr = jedis.incr("k1");
        System.out.println("递增后的结果:" + incr);

        //关闭资源
        jedis.close();

    }
}
