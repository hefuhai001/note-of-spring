package com.example.boot;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.junit.jupiter.api.Test;

public class TestChat {

    @Test
    void test() {
        ChatLanguageModel model = OpenAiChatModel
                .builder()
                .apiKey("demo")
                .modelName("gpt-4o-mini")
                .build();

        String answer = model.chat("你好，你是谁？");
        System.out.println(answer);
    }


    @Test
    void test01() {
        ChatLanguageModel model = QwenChatModel
                .builder()
                //.baseUrl("https://dashscope.aliyuncs.com")
                .apiKey("sk-4b6145929a2c40f8870be4f302570c7d")
                .modelName("qwen-plus")
                .build();

        String answer = model.chat("你好，你是谁？");
        System.out.println(answer);
    }


    @Test
    void test02() {
        ChatLanguageModel model = QwenChatModel
                .builder()
                //.baseUrl("https://dashscope.aliyuncs.com")
                .apiKey("sk-4b6145929a2c40f8870be4f302570c7d")
                .modelName("deepseek-v3")
                .build();

        String answer = model.chat("你好，你是谁？");
        System.out.println(answer);
    }

}
