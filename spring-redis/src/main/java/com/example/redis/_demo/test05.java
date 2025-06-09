package com.example.redis._demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class test05 {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("test05")
    public void test05() {

        /*    //删除指定的key
        Boolean aBoolean = stringRedisTemplate.delete("k1");
        System.out.println("是否删除成功:"+aBoolean);
        //判断指定的key是否存在
        Boolean hasKey = stringRedisTemplate.hasKey("k1");
        System.out.println("判断指定的key是否存在:"+hasKey);
*/
        //获取对string类型操作的类对象
        ValueOperations<String, String> forValue = stringRedisTemplate.opsForValue();
        forValue.set("n1", "测试数据1");
        forValue.set("n2", "2");
        forValue.set("n3", "测试数据3");

        //如果存在 则不存入 不存在则存入
        Boolean aBoolean = forValue.setIfAbsent("n4", "测试数据4", 25, TimeUnit.SECONDS);
        System.out.println("是否存入成功 " + aBoolean);

        //获取对应的值
        String n1 = forValue.get("n1");
        System.out.println("n1 = " + n1);

        //递增
        Long n2 = forValue.increment("n2");
        System.out.println("n2递增后的值" + n2);

    }
}
