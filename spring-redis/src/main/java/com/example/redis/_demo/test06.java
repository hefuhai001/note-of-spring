package com.example.redis._demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class test06 {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("test06")
    public void test06() {

        //对于hash类型的操作
        HashOperations<String, Object, Object> forHash = stringRedisTemplate.opsForHash();

        //加入数据
        forHash.put("h1","name","刘德华");
        forHash.put("h1","age","18");
        forHash.put("h1","address","山东");

        HashMap<String,String> map = new HashMap<>();
        map.put("name","老六");
        map.put("age","19");
        map.put("adress","南京");
        forHash.putAll("h2",map);


        Object o = forHash.get("h1", "name");
        System.out.println("获取指定key对于的name的值:"+o);

        Map<Object, Object> h2 = forHash.entries("h2");
        System.out.println("获取h2对于的map对象:"+h2);

        Set<Object> keys = forHash.keys("h2");
        System.out.println("获取h2对于的所以field："+keys);

        List<Object> values = forHash.values("h2");
        System.out.println("获取h2对于的所有filed的值:"+values);

    }
}
