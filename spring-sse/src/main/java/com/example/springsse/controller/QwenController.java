package com.example.springsse.controller;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.common.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/qwen")
public class QwenController {

    @Value("${model.qwen.key}")
    private String apiKey;

    @Value("${model.qwen.id}")
    private String model;

    private static final Logger logger = LoggerFactory.getLogger(QwenController.class);

    @PostMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamCallWithMessage(@RequestBody List<Message> messagesData) {
        try {
            // 将自定义Message对象转换为DashScope的Message对象
            List<Message> messages = messagesData.stream()
                    .map(msg -> Message.builder()
                            .role(msg.getRole())
                            .content(msg.getContent())
                            .build())
                    .collect(Collectors.toList());

            Generation gen = new Generation();
            GenerationParam param = buildGenerationParam(messages);

            return Flux.from(gen.streamCall(param))
                    .map(message -> {
                        String content = message.getOutput().getChoices().get(0).getMessage().getContent();
                        return ServerSentEvent.<String>builder()
                                .id(LocalDateTime.now().toString())
                                .event("message")
                                .data(content)
                                .build();
                    })
                    .onErrorResume(e -> {
                        logger.error("Error during streaming: {}", e.getMessage());
                        return Flux.just(ServerSentEvent.<String>builder()
                                .id(LocalDateTime.now().toString())
                                .event("error")
                                .data("Error: " + e.getMessage())
                                .build());
                    });
        } catch (Exception e) {
            logger.error("Error initializing stream: {}", e.getMessage());
            return Flux.just(ServerSentEvent.<String>builder()
                    .id(LocalDateTime.now().toString())
                    .event("error")
                    .data("Error: " + e.getMessage())
                    .build());
        }
    }

    private GenerationParam buildGenerationParam(List<Message> messages) {
        return GenerationParam.builder()
                // 若没有配置环境变量，请用百炼API Key将下行替换为：.apiKey("sk-xxx")
                .apiKey(apiKey)
                .model(model)   // 此处以qwen-plus为例，您可按需更换模型名称。模型列表：https://help.aliyun.com/zh/model-studio/getting-started/models
                .messages(messages)
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                .incrementalOutput(true)
                .build();
    }


}
