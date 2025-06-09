package com.example.springsse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
public class SseController {

    @GetMapping("/sse")
    public SseEmitter handleSse() {
        SseEmitter emitter = new SseEmitter();
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        // 定义定时任务，每隔1秒发送数据
        Runnable task = () -> {
            try {
                String data = "Time: " + LocalDateTime.now();
                emitter.send(
                        SseEmitter.event()
                                .data(data)
                                .id(String.valueOf(System.currentTimeMillis()))
                );
            } catch (IOException e) {
                emitter.completeWithError(e); // 发生错误时关闭连接
                executor.shutdown();
            }
        };

        // 启动任务，立即执行并每隔1秒重复
        executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);

        // 注册回调：客户端断开时关闭任务和线程池
        emitter.onCompletion(executor::shutdown);
        emitter.onTimeout(executor::shutdown);

        return emitter;
    }
}