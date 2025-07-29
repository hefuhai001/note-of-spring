package com.example.sse.controller;

import com.example.sse.ai.AiConfig;
import com.example.sse.ai.ModelFactory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/chat")
@Slf4j
@RequiredArgsConstructor
public class ChatController {

    private final ModelFactory modelFactory;
    @Autowired
    private AiConfig aiConfig;
    @Autowired
    private ChatMemoryStore chatMemoryStore;

    // 使用 Map 存储每个用户的 Sinks.Many<String> 实例
    private final Map<Integer, Sinks.Many<String>> userSinks = Collections.synchronizedMap(new HashMap<>());

    @GetMapping("/connect/{userId}")
    public Flux<String> connect(@PathVariable Integer userId) {
        // 为每个用户创建独立的 Sinks.Many<String> 实例
        userSinks.computeIfAbsent(userId, k -> Sinks.many().multicast().directBestEffort());
        return userSinks.get(userId).asFlux();
    }

    @GetMapping("/send/{userId}")
    public String streamMemoryUserStore(
            @RequestParam(defaultValue = "我是Alice") String message,
            @RequestParam(defaultValue = "wen") String provider,
            @PathVariable Integer userId
    ) {
        log.info("Provider: {}, User ID: {}", provider, userId);

        ChatLanguageModel model = modelFactory.createModel(provider);
        StreamingChatLanguageModel streamModel = modelFactory.createStreamModel(provider);
        AiConfig.AssistantUnique assistantUnique = aiConfig.assistantUniqueStore(model, streamModel, chatMemoryStore);
        TokenStream stream = assistantUnique.stream(userId, message);

        Sinks.Many<String> sink = userSinks.get(userId);
        if (sink == null) {
            return "用户未连接";
        }

        stream.onPartialResponse(s -> sink.tryEmitNext(s))
                .onCompleteResponse((s) -> {
                    log.info("Sending complete response to user: {}", userId);
                })
                .onError(e -> {
                    log.error("Error sending response to user: {}", userId, e);
                })
                .start();

        return "开始发送数据给用户: " + userId;
    }


}