package com.example.sse.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/flex")
public class FlexController {

    private final Sinks.Many<String> sink = Sinks.many().multicast().directBestEffort();

    @GetMapping("/connect")
    public Flux<String> connect() {
        return sink.asFlux();
    }

    @GetMapping("/send")
    public String send() {
        for (int i = 0; i <= 10; i++) {
            sink.tryEmitNext("进度: " + i + "%");
            try {
                Thread.sleep(1000); // 模拟延迟
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "开始发送数据";
    }


    // 使用 Map 存储每个用户的 Sinks.Many<String> 实例
    private final Map<String, Sinks.Many<String>> userSinks = Collections.synchronizedMap(new HashMap<>());

    @GetMapping("/connect/{userId}")
    public Flux<String> connect(@PathVariable String userId) {
        // 为每个用户创建独立的 Sinks.Many<String> 实例
        userSinks.computeIfAbsent(userId, k -> Sinks.many().multicast().directBestEffort());
        return userSinks.get(userId).asFlux();
    }


    @GetMapping("/send/{userId}")
    public String send(@PathVariable String userId) {
        Sinks.Many<String> sink = userSinks.get(userId);
        if (sink == null) {
            return "用户未连接";
        }

        for (int i = 0; i <= 10; i++) {
            sink.tryEmitNext("进度: " + i + "%");
            try {
                Thread.sleep(1000); // 模拟延迟
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "开始发送数据给用户: " + userId;
    }

}