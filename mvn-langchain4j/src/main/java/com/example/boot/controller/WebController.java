package com.example.boot.controller;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @GetMapping("/web")
    public String web() {
        return "Hello World";
    }

    @GetMapping("/gpt")
    public String gpt(@RequestParam(defaultValue = "你好，你是谁？") String question) {
        ChatLanguageModel model = OpenAiChatModel
                .builder()
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();
        return model.chat(question);
    }


    @GetMapping("/wen")
    public String wen(@RequestParam(defaultValue = "你好，你是谁？") String question) {
        ChatLanguageModel model = QwenChatModel
                .builder()
                //.baseUrl("https://dashscope.aliyuncs.com")
                .apiKey("sk-4b6145929a2c40f8870be4f302570c7d")
                .modelName("qwen-plus")
                .build();
        return model.chat(question);
    }


    @GetMapping("/dsc")
    public String dsc(@RequestParam(defaultValue = "你好，你是谁？") String question) {
        ChatLanguageModel model = QwenChatModel
                .builder()
                //.baseUrl("https://dashscope.aliyuncs.com")
                .apiKey("sk-4b6145929a2c40f8870be4f302570c7d")
                .modelName("deepseek-v3")
                .build();
        return model.chat(question);
    }


}
