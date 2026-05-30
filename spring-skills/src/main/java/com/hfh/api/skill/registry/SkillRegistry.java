package com.hfh.api.skill.registry;

import com.hfh.api.skill.config.SkillProperties;
import com.hfh.api.skill.model.Skill;
import com.hfh.api.skill.model.SkillApiConfig;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SkillRegistry {

    private static final Logger log = LoggerFactory.getLogger(SkillRegistry.class);

    private final SkillProperties skillProperties;
    private final Map<String, Skill> skillMap = new ConcurrentHashMap<>();

    public SkillRegistry(SkillProperties skillProperties) {
        this.skillProperties = skillProperties;
    }

    @PostConstruct
    public void discoverAndRegisterSkills() {
        if (!skillProperties.isAutoDiscovery()) {
            log.info("Skill auto-discovery is disabled");
            return;
        }

        log.info("Starting skill auto-discovery from path: {}", skillProperties.getBasePath());

        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            String pattern = skillProperties.getBasePath() + "*/" + skillProperties.getMetadataFileName();
            Resource[] resources = resolver.getResources(pattern);

            for (Resource resource : resources) {
                try {
                    Skill skill = parseSkillMetadata(resource);
                    if (skill != null) {
                        String promptContent = loadPromptTemplate(skill.getBasePath());
                        skill.setPromptTemplate(promptContent);
                        registerSkill(skill);
                    }
                } catch (Exception e) {
                    log.error("Failed to parse skill from resource: {}", resource.getURL(), e);
                }
            }

            log.info("Skill auto-discovery completed. Registered {} skills: {}", skillMap.size(), skillMap.keySet());
        } catch (Exception e) {
            log.error("Failed to discover skills", e);
        }
    }

    private Skill parseSkillMetadata(Resource resource) {
        try {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

            Skill skill = new Skill();
            skill.setName((String) data.get("name"));
            skill.setDescription((String) data.get("description"));
            skill.setVersion((String) data.get("version"));
            skill.setAuthor((String) data.get("author"));
            skill.setCategory((String) data.get("category"));

            @SuppressWarnings("unchecked")
            Map<String, String> parameters = (Map<String, String>) data.get("parameters");
            if (parameters != null) {
                skill.setParameters(parameters);
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> apiData = (Map<String, Object>) data.get("api");
            if (apiData != null) {
                SkillApiConfig apiConfig = new SkillApiConfig();
                apiConfig.setUrl((String) apiData.get("url"));
                apiConfig.setMethod((String) apiData.get("method"));
                apiConfig.setBody((String) apiData.get("body"));
                apiConfig.setResponseKey((String) apiData.get("response_key"));
                @SuppressWarnings("unchecked")
                Map<String, String> apiHeaders = (Map<String, String>) apiData.get("headers");
                if (apiHeaders != null) {
                    apiConfig.setHeaders(apiHeaders);
                }
                skill.setApiConfig(apiConfig);
                log.debug("Parsed API config for skill '{}': {}", skill.getName(), apiConfig);
            }

            String url = resource.getURL().toString();
            String basePath = url.substring(0, url.lastIndexOf("/") + 1);
            skill.setBasePath(basePath);

            log.debug("Parsed skill metadata: name={}, version={}, category={}", skill.getName(), skill.getVersion(), skill.getCategory());
            return skill;
        } catch (Exception e) {
            log.error("Failed to parse skill metadata", e);
            return null;
        }
    }

    private String loadPromptTemplate(String basePath) {
        try {
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            String promptPattern = basePath + skillProperties.getPromptFileName();
            Resource[] resources = resolver.getResources(promptPattern);

            if (resources.length > 0) {
                return new String(resources[0].getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            }

            log.warn("No prompt file found at: {}", promptPattern);
            return "";
        } catch (Exception e) {
            log.error("Failed to load prompt template from: {}", basePath, e);
            return "";
        }
    }

    public void registerSkill(Skill skill) {
        if (skill.getName() == null || skill.getName().isBlank()) {
            log.warn("Cannot register skill without a name");
            return;
        }
        skillMap.put(skill.getName(), skill);
        log.info("Registered skill: {} v{}", skill.getName(), skill.getVersion());
    }

    public void unregisterSkill(String name) {
        Skill removed = skillMap.remove(name);
        if (removed != null) {
            log.info("Unregistered skill: {}", name);
        }
    }

    public Optional<Skill> getSkill(String name) {
        return Optional.ofNullable(skillMap.get(name));
    }

    public List<Skill> getAllSkills() {
        return new ArrayList<>(skillMap.values());
    }

    public boolean hasSkill(String name) {
        return skillMap.containsKey(name);
    }

    public int getSkillCount() {
        return skillMap.size();
    }
}
