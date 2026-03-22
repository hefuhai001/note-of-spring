package com.example.llm.config;

import com.example.llm.skills.Skill;
import com.example.llm.skills.SkillRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SkillAutoConfiguration {

    private final SkillRegistry registry;
    private final List<Skill> skills;  // 自动注入所有 Skill Bean

    @PostConstruct
    public void registerAllSkills() {
        skills.forEach(registry::register);
        log.info("Auto-registered {} skills", skills.size());
    }
}