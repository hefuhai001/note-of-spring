package com.example.llm.service;

import com.example.llm.config.LlmConfig;
import com.example.llm.dto.ChatRequest;
import com.example.llm.dto.ChatResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LlmService {

    private final LlmConfig config;

    private WebClient webClient() {
        return WebClient.builder()
                .baseUrl(config.getBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + config.getApiKey())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * 发送聊天请求（支持 Tools/Skills）
     */
    public Mono<ChatResponse> chat(List<ChatRequest.Message> messages,
                                   List<ChatRequest.Tool> tools) {

        ChatRequest request = ChatRequest.builder()
                .model(config.getModel())
                .messages(messages)
                .tools(tools)
                .temperature(0.7)
                .build();

        log.debug("Sending request to LLM: {}", request);

        return webClient()
                .post()
                .uri("/v1/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .doOnNext(response -> log.debug("Received response: {}", response))
                .onErrorResume(e -> {
                    log.error("LLM API error", e);
                    return Mono.error(new RuntimeException("大模型调用失败: " + e.getMessage()));
                });
    }

    /**
     * 简单对话（不使用 Tools）
     */
    public Mono<ChatResponse> chat(List<ChatRequest.Message> messages) {
        return chat(messages, null);
    }
}