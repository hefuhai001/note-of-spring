package com.example.danmaku.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * 对于敏感内容过滤，可以实现更复杂的过滤系统：
 */
@Service
public class ContentFilterService {

    private Set<String> sensitiveWords;

    @PostConstruct
    public void init() {
        // 从配置文件或数据库加载敏感词
        sensitiveWords = new HashSet<>();
        sensitiveWords.add("敏感词1");
        sensitiveWords.add("敏感词2");
        sensitiveWords.add("敏感词3");
        // 可以从外部文件加载更多敏感词
    }

    public String filterContent(String content) {
        if (content == null || content.isEmpty()) {
            return content;
        }

        String filteredContent = content;
        for (String word : sensitiveWords) {
            filteredContent = filteredContent.replaceAll(word, "***");
        }

        return filteredContent;
    }

    // 添加敏感词
    public void addSensitiveWord(String word) {
        sensitiveWords.add(word);
    }

    // 移除敏感词
    public void removeSensitiveWord(String word) {
        sensitiveWords.remove(word);
    }
}
