package com.example.boot.config;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    /*
     * 记忆存储
     * */
    public interface Assistant {
        String chat(String message);
        TokenStream stream(String message);
    }

    public Assistant assistant(ChatLanguageModel chatLanguageModel, StreamingChatLanguageModel streamingChatLanguageModel) {
        MessageWindowChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(chatLanguageModel)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                .chatMemory(chatMemory)
                .build();
        return assistant;
    }


    /*
     * 记忆存储
     * 用户区分
     * */
    public interface AssistantUnique {
        String chat(@MemoryId int memoryId, @UserMessage String message);
        TokenStream stream(@MemoryId int memoryId, @UserMessage String message);
    }

    public AssistantUnique assistantUnique(ChatLanguageModel chatLanguageModel, StreamingChatLanguageModel streamingChatLanguageModel) {

        AssistantUnique assistantUnique = AiServices.builder(AssistantUnique.class)
                .chatLanguageModel(chatLanguageModel)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                .chatMemoryProvider(memoryId ->
                        MessageWindowChatMemory.builder().maxMessages(10)
                                .id(memoryId).build()
                )
                .build();
        return assistantUnique;
    }



}
