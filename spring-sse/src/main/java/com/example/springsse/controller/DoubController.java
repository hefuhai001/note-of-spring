package com.example.springsse.controller;

import com.example.springsse.entity.Message;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/doub")
public class DoubController {

    @Value("${model.doub.key}")
    private String apiKey;

    @Value("${model.doub.id}")
    private String model;

    private ArkService service;

    @PostConstruct
    public void init() {
        this.service = ArkService.builder().apiKey(apiKey).build();
    }

    @PreDestroy
    public void cleanup() {
        if (service != null) {
            service.shutdownExecutor();
        }
    }

    @PostMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamCompletion(@RequestBody List<Message> messages) {
        // 转换输入消息为Ark所需的ChatMessage格式（带错误处理）
        List<ChatMessage> chatMessages = messages.stream()
                .map(msg -> {
                    try {
                        return ChatMessage.builder()
                                .role(ChatMessageRole.valueOf(msg.getRole().toUpperCase()))
                                .content(msg.getContent())
                                .build();
                    } catch (IllegalArgumentException e) {
                        return ChatMessage.builder()
                                .role(ChatMessageRole.USER)
                                .content(msg.getContent())
                                .build();
                    }
                })
                .collect(Collectors.toList());

        // 创建请求
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(model)
                .messages(chatMessages)
                .build();

        return Flux.create(sink -> {
            service.streamChatCompletion(request)
                    .doOnError(sink::error)
                    .subscribe(
                            choice -> {
                                if (!choice.getChoices().isEmpty()) {
                                    String content = (String) choice.getChoices().get(0).getMessage().getContent();
                                    sink.next(ServerSentEvent.<String>builder()
                                            .id(LocalDateTime.now().toString())
                                            .event("message")
                                            .data(content)
                                            .build());
                                }
                            },
                            sink::error,
                            sink::complete
                    );
        });
    }


}
