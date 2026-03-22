package com.example.llm.skills;

import com.example.llm.dto.ChatRequest;
import com.example.llm.dto.SkillResult;
import java.util.Map;

/**
 * Skill 接口 - 定义大模型可调用的工具能力
 */
public interface Skill {

    /**
     * Skill 唯一标识名
     */
    String getName();

    /**
     * Skill 描述，帮助大模型理解何时调用
     */
    String getDescription();

    /**
     * 参数定义 (JSON Schema格式)
     */
    Map<String, Object> getParametersSchema();

    /**
     * 执行 Skill
     * @param arguments 大模型提取的参数
     * @return 执行结果
     */
    SkillResult execute(Map<String, Object> arguments);

    /**
     * 转换为 OpenAI Function 格式
     */
    default ChatRequest.Tool toTool() {
        return ChatRequest.Tool.builder()
                .type("function")
                .function(ChatRequest.Tool.Function.builder()
                        .name(getName())
                        .description(getDescription())
                        .parameters(getParametersSchema())
                        .build())
                .build();
    }
}