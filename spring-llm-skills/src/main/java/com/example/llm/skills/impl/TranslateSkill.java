package com.example.llm.skills.impl;


import com.example.llm.dto.SkillResult;
import com.example.llm.skills.Skill;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TranslateSkill implements Skill {
    @Override
    public String getName() {
        return "translate";
    }

    @Override
    public String getDescription() {
        return "翻译文本到指定语言";
    }

    @Override
    public Map<String, Object> getParametersSchema() {
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "text", Map.of("type", "string"),
                        "target_lang", Map.of("type", "string", "enum", List.of("zh", "en", "ja"))
                ),
                "required", List.of("text", "target_lang")
        );
    }

    @Override
    public SkillResult execute(Map<String, Object> args) {
        // 调用翻译API或本地逻辑
        return SkillResult.success(getName(),
                Map.of("translated", "翻译结果..."));
    }
}