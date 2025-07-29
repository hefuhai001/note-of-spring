package com.example.sse.ai;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.community.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelFactory {

    private final String gptKey = "demo";
    private final String gptName = "gpt-4o-mini";
    private final String wenKey = "sk-4b6145929a2c40f8870be4f302570c7d";
    private final String wenName = "qwen-plus";
    private final String dscKey = "sk-4b6145929a2c40f8870be4f302570c7d";
    private final String dscName = "deepseek-v3";

    public ChatLanguageModel createModel(String provider) {
        return switch (provider) {
            case "gpt" -> OpenAiChatModel.builder()
                    .apiKey(gptKey)
                    .modelName(gptName)
                    .build();
            case "wen" -> QwenChatModel.builder()
                    .apiKey(wenKey)
                    .modelName(wenName)
                    .build();
            case "dsc" -> QwenChatModel.builder()
                    .apiKey(dscKey)
                    .modelName(dscName)
                    .build();

            default -> throw new IllegalArgumentException("Unsupported provider");
        };
    }


    public StreamingChatLanguageModel createStreamModel(String provider) {
        return switch (provider) {
            case "gpt" -> OpenAiStreamingChatModel.builder()
                    .apiKey(gptKey)
                    .modelName(gptName)
                    .build();
            case "wen" -> QwenStreamingChatModel.builder()
                    .apiKey(wenKey)
                    .modelName(wenName)
                    .build();
            case "dsc" -> QwenStreamingChatModel.builder()
                    .apiKey(dscKey)
                    .modelName(dscName)
                    .build();
            default -> throw new IllegalArgumentException("Unsupported provider");
        };
    }

}