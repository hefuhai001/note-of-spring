package com.example.sse.ai;

import com.example.sse.repository.ChatMessageRepository;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.TokenStream;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    /**
     * 记忆存储 用户区分
     */
    public interface AssistantUnique {
        String chat(@MemoryId int memoryId, @UserMessage String message);

        TokenStream stream(@MemoryId int memoryId, @UserMessage String message);
    }

    @Bean
    public AssistantUnique assistantUnique(ChatLanguageModel chatLanguageModel, StreamingChatLanguageModel streamingChatLanguageModel) {

        //为Assistant.动态代理对象chat-->对话内容存储ChatMemory---> 聊天记录ChatMemory.取出来-->放入到当前对话中
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


    /**
     * 注入持久化存储实现
     */
    @Bean
    public ChatMemoryStore chatMemoryStore(ChatMessageRepository repository) {
        return new PersistentChatMemorystore(repository);
    }

    /**
     * 配置带持久化的Assistant
     */
    @Bean
    public AssistantUnique assistantUniqueStore(
            ChatLanguageModel chatLanguageModel,
            StreamingChatLanguageModel streamingChatLanguageModel,
            ChatMemoryStore chatMemoryStore) { // 注入持久化存储

        ChatMemoryProvider chatMemoryProvider = memoryId ->
                MessageWindowChatMemory.builder()
                        .id(memoryId)
                        .maxMessages(50)
                        .chatMemoryStore(chatMemoryStore) // 使用注入的持久化存储
                        .build();

        return AiServices.builder(AssistantUnique.class)
                .chatLanguageModel(chatLanguageModel)
                .streamingChatLanguageModel(streamingChatLanguageModel)
                .chatMemoryProvider(chatMemoryProvider)
                .build();
    }


}
