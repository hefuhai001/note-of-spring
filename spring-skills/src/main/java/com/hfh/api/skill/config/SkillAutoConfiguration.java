package com.hfh.api.skill.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SkillAutoConfiguration {

    @Bean
    public ChatClient chatClient(org.springframework.ai.chat.model.ChatModel chatModel) {
        return ChatClient.builder(chatModel).build();
    }
}
