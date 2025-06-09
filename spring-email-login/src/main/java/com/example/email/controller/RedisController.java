package com.example.email.controller;

import com.example.email.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/set")
    public String set(@RequestParam String key, @RequestParam String value) {
        redisService.set(key, value);
        return "Set successfully";
    }

    @GetMapping("/get")
    public Object get(@RequestParam String key) {
        return redisService.get(key);
    }

    @GetMapping("/delete")
    public String delete(@RequestParam String key) {
        redisService.delete(key);
        return "Deleted successfully";
    }

    @GetMapping("/setWithExpire")
    public String setWithExpire(@RequestParam String key, @RequestParam String value, @RequestParam long timeout) {
        redisService.setWithExpire(key, value, timeout, TimeUnit.SECONDS);
        return "Set with expire successfully";
    }

}
