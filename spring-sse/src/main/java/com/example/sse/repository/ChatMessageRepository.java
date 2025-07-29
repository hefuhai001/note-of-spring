package com.example.sse.repository;

import com.example.sse.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    List<ChatMessageEntity> findByMemoryIdOrderByCreatedAtAsc(String memoryId);

    void deleteByMemoryId(String memoryId);

}
