package com.example.sse.ai;

import com.example.sse.entity.ChatMessageEntity;
import com.example.sse.entity.MessageRole;
import com.example.sse.repository.ChatMessageRepository;
import dev.langchain4j.data.message.*;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PersistentChatMemorystore implements ChatMemoryStore {

    private final ChatMessageRepository repository;

    // 推荐使用依赖注入
    public PersistentChatMemorystore(ChatMessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        List<ChatMessageEntity> entities = repository.findByMemoryIdOrderByCreatedAtAsc(toString(memoryId));
        return entities.stream()
                .map(this::toChatMessage)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateMessages(Object memoryId, List<ChatMessage> messages) {
        String idStr = toString(memoryId);

        // 先删除旧消息
        repository.deleteByMemoryId(idStr);

        // 转换并保存新消息
        List<ChatMessageEntity> entities = messages.stream()
                .map(msg -> toEntity(idStr, msg))
                .collect(Collectors.toList());

        repository.saveAll(entities);
    }

    @Override
    @Transactional
    public void deleteMessages(Object memoryId) {
        repository.deleteByMemoryId(toString(memoryId));
    }

    private String toString(Object memoryId) {
        return memoryId.toString();  // 统一转换为字符串存储
    }

    private ChatMessageEntity toEntity(String memoryId, ChatMessage message) {
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setMemoryId(memoryId);
        entity.setRole(resolveRole(message));
        entity.setContent(ChatMessageSerializer.messageToJson(message));
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }

    private MessageRole resolveRole(ChatMessage message) {
        if (message instanceof UserMessage) return MessageRole.USER;
        if (message instanceof AiMessage) return MessageRole.AI;
        if (message instanceof SystemMessage) return MessageRole.SYSTEM;
        if (message instanceof ToolExecutionResultMessage) return MessageRole.TOOL_EXECUTION_RESULT;
        throw new IllegalArgumentException("Unknown message type");
    }

    private ChatMessage toChatMessage(ChatMessageEntity entity) {
        return ChatMessageDeserializer.messageFromJson(entity.getContent());
    }

}
