package com.example.redis._demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test07 {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("test07")
    public void test07() {

        //默认RedisTemplate它的key和value的序列化都是使用的JdkSerializationRedisSerializer方式，
        // 该序列化要求类必须实现Serializable接口。
        //我们在实际开发中，我们的key都是String类型，我们应该指定String序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        ValueOperations valueOperations = redisTemplate.opsForValue();
        valueOperations.set("student1", new Student(1, "张三", "男"));
        System.out.println(valueOperations.get("student1"));

    }
}
