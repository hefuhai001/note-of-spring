package com.example.boot.controller;

import com.example.boot.config.AiConfig;
import com.example.boot.config.ApiResponse;
import com.example.boot.config.ModelFactory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.service.TokenStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UnifiedChatController {

    private final ModelFactory modelFactory;

    @GetMapping("/chat")
    public ApiResponse<String> chat(
            @RequestParam(defaultValue = "你好，你是谁？") String message,
            @RequestParam(defaultValue = "gpt") String provider
    ) {
        return ApiResponse.success(
                modelFactory.createModel(provider).chat(message)
        );
    }


    @GetMapping(value = "/stream", produces = "text/stream;charset=UTF-8")
    public Flux<String> stream(
            @RequestParam(defaultValue = "你好，你是谁？") String message,
            @RequestParam(defaultValue = "wen") String provider) {

        return Flux.create(fluxSink -> {
            modelFactory.createStreamModel(provider).chat(message, new StreamingChatResponseHandler() {
                @Override
                public void onPartialResponse(String s) {
                    fluxSink.next(s);
                }

                @Override
                public void onCompleteResponse(ChatResponse chatResponse) {
                    fluxSink.complete();
                }

                @Override
                public void onError(Throwable throwable) {
                    fluxSink.error(throwable);
                }
            });
        });
    }

    @Autowired
    AiConfig aiConfig;

    @GetMapping(value = "/stream-memory", produces = "text/stream;charset=UTF-8")
    public Flux<String> streamMemory(
            @RequestParam(defaultValue = "我是Alice") String message,
            @RequestParam(defaultValue = "wen") String provider
    ) {

        ChatLanguageModel model = modelFactory.createModel(provider);
        StreamingChatLanguageModel streamModel = modelFactory.createStreamModel(provider);
        TokenStream stream = aiConfig.assistant(model, streamModel).stream(message);

        return Flux.create(sink -> {
            stream.onPartialResponse(s -> sink.next(s))
                    .onCompleteResponse(c -> sink.complete())
                    .onError(sink::error)
                    .start();
        });
    }


}
