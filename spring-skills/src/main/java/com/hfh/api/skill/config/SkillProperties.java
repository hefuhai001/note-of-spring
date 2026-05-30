package com.hfh.api.skill.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "skills")
public class SkillProperties {

    private String basePath = "classpath:/skills/";
    private boolean autoDiscovery = true;
    private String promptFileName = "prompt.md";
    private String metadataFileName = "skill.yaml";

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public boolean isAutoDiscovery() {
        return autoDiscovery;
    }

    public void setAutoDiscovery(boolean autoDiscovery) {
        this.autoDiscovery = autoDiscovery;
    }

    public String getPromptFileName() {
        return promptFileName;
    }

    public void setPromptFileName(String promptFileName) {
        this.promptFileName = promptFileName;
    }

    public String getMetadataFileName() {
        return metadataFileName;
    }

    public void setMetadataFileName(String metadataFileName) {
        this.metadataFileName = metadataFileName;
    }
}
