package com.hfh.api.skill.service;

import com.hfh.api.skill.executor.SkillApiExecutor;
import com.hfh.api.skill.model.Skill;
import com.hfh.api.skill.model.SkillApiConfig;
import com.hfh.api.skill.model.SkillExecutionRequest;
import com.hfh.api.skill.model.SkillExecutionResponse;
import com.hfh.api.skill.registry.SkillRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class SkillService {

    private static final Logger log = LoggerFactory.getLogger(SkillService.class);

    private final SkillRegistry skillRegistry;
    private final ChatClient chatClient;
    private final SkillApiExecutor skillApiExecutor;

    public SkillService(SkillRegistry skillRegistry, ChatClient chatClient, SkillApiExecutor skillApiExecutor) {
        this.skillRegistry = skillRegistry;
        this.chatClient = chatClient;
        this.skillApiExecutor = skillApiExecutor;
    }

    public List<Skill> listSkills() {
        return skillRegistry.getAllSkills();
    }

    public Optional<Skill> getSkill(String name) {
        return skillRegistry.getSkill(name);
    }

    public SkillExecutionResponse executeSkill(SkillExecutionRequest request) {
        String skillName = request.getSkillName();
        log.info("Executing skill: {}", skillName);

        Optional<Skill> skillOpt = skillRegistry.getSkill(skillName);
        if (skillOpt.isEmpty()) {
            log.warn("Skill not found: {}", skillName);
            return SkillExecutionResponse.failure(skillName, "Skill not found: " + skillName);
        }

        Skill skill = skillOpt.get();

        String apiResponse = null;
        SkillApiConfig apiConfig = skill.getApiConfig();
        if (apiConfig != null && apiConfig.getUrl() != null) {
            log.info("Skill '{}' has API config, calling external API: {}", skillName, apiConfig.getUrl());
            apiResponse = skillApiExecutor.execute(apiConfig);
            if (apiResponse == null) {
                log.warn("API call returned null for skill: {}", skillName);
                return SkillExecutionResponse.failure(skillName, "API call failed for: " + apiConfig.getUrl());
            }
        }

        Map<String, String> variables = request.getVariables();
        if (apiResponse != null) {
            if (variables != null) {
                variables.put("api_response", apiResponse);
            } else {
                variables = Map.of("api_response", apiResponse);
            }
        }

        String resolvedPrompt = resolvePromptTemplate(skill.getPromptTemplate(), variables, request.getUserInput());

        try {
            String userInput = request.getUserInput();
            if (userInput == null || userInput.isBlank()) {
                if (apiResponse != null) {
                    userInput = apiResponse;
                } else {
                    userInput = "";
                }
            }

            String result = chatClient.prompt()
                    .system(resolvedPrompt)
                    .user(userInput)
                    .call()
                    .content();

            log.info("Skill execution completed: {}", skillName);
            return SkillExecutionResponse.success(skillName, result);
        } catch (Exception e) {
            log.error("Skill execution failed: {}", skillName, e);
            return SkillExecutionResponse.failure(skillName, "Execution failed: " + e.getMessage());
        }
    }

    public String getSkillPrompt(String name) {
        Optional<Skill> skillOpt = skillRegistry.getSkill(name);
        if (skillOpt.isEmpty()) {
            return null;
        }
        return skillOpt.get().getPromptTemplate();
    }

    private String resolvePromptTemplate(String template, Map<String, String> variables, String userInput) {
        if (template == null || template.isEmpty()) {
            return "";
        }

        String resolved = template;
        if (variables != null) {
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                resolved = resolved.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
        }

        if (userInput != null) {
            resolved = resolved.replace("{{user_input}}", userInput);
        }

        return resolved;
    }
}
