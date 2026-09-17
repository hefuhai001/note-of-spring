package com.example.hfh.skills;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SkillRegistry {

    private final Map<String, Skill> skills = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        log.info("Skill Registry initialized");
    }

    /**
     * 注册 Skill
     */
    public void register(Skill skill) {
        skills.put(skill.getName(), skill);
        log.info("Registered skill: {}", skill.getName());
    }

    /**
     * 获取所有 Skills 作为 Tools
     */
    public List<com.example.hfh.dto.ChatRequest.Tool> getAllTools() {
        return skills.values().stream()
                .map(Skill::toTool)
                .toList();
    }

    /**
     * 执行指定 Skill
     */
    public com.example.hfh.dto.SkillResult execute(String skillName, Map<String, Object> arguments) {
        Skill skill = skills.get(skillName);
        if (skill == null) {
            return com.example.hfh.dto.SkillResult.fail(skillName, "Skill not found: " + skillName);
        }
        try {
            return skill.execute(arguments);
        } catch (Exception e) {
            log.error("Skill execution failed: {}", skillName, e);
            return com.example.hfh.dto.SkillResult.fail(skillName, e.getMessage());
        }
    }

    public boolean hasSkill(String name) {
        return skills.containsKey(name);
    }
}