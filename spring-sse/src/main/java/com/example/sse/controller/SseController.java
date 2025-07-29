package com.example.sse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/sse")
public class SseController {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @GetMapping("/connect")
    public SseEmitter connect() {
        SseEmitter emitter = new SseEmitter();
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        return emitter;
    }

    @GetMapping("/send")
    public String send() {
        executorService.execute(() -> {
            for (int i = 0; i <= 10; i++) {
                try {
                    Thread.sleep(1000); // 模拟延迟
                    for (SseEmitter sseEmitter : emitters) {
                        sseEmitter.send("进度: " + i + "%");
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return "开始发送数据";
    }


    // 使用 Map 存储每个用户的 SseEmitter 实例，以用户ID为键
    private final Map<String, SseEmitter> userEmitters = Collections.synchronizedMap(new HashMap<>());

    @GetMapping("/connect/{userId}")
    public SseEmitter connect(@PathVariable String userId) {
        SseEmitter emitter = new SseEmitter();
        userEmitters.put(userId, emitter); // 将用户的 emitter 存储到 Map 中

        // 添加回调，以便在连接完成、超时或出错时移除 emitter
        emitter.onCompletion(() -> userEmitters.remove(userId));
        emitter.onTimeout(() -> userEmitters.remove(userId));
        emitter.onError(e -> userEmitters.remove(userId));

        return emitter;
    }

    @GetMapping("/send/{userId}")
    public String send(@PathVariable String userId) {
        SseEmitter emitter = userEmitters.get(userId);
        if (emitter == null) {
            return "用户未连接";
        }

        executorService.execute(() -> {
            for (int i = 0; i <= 10; i++) {
                try {
                    Thread.sleep(1000); // 模拟延迟
                    emitter.send("进度: " + i + "%");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        return "开始发送数据给用户: " + userId;
    }
}