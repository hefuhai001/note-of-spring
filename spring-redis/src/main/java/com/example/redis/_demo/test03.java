package com.example.redis._demo;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class test03 {

    @GetMapping("test03")
    public void test03(){

        /*
        * 对应hash类型的操作----可以存放对象。
        * 连接方式通过jar包直接连接
        * */
        //配置连接服务
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        //输入设置的数据库密码
        jedis.auth("rootXH");

        String name = jedis.hget("myhash", "name");
        System.out.println("获取hash中name对应的值:" + name);


        Map<String, String> map1 = jedis.hgetAll("myhash");
        System.out.println("获取指定key对应的内容:" + map1);

        Set<String> k11 = jedis.hkeys("myhash");
        System.out.println("获取myhash对应的所有field:" + k11);

        List<String> values = jedis.hvals("myhash");
        System.out.println("获取myhash对应的所有field的值:" + values);

        //关闭资源
        jedis.close();
    }
}
