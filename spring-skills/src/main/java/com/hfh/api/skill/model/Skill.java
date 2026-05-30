package com.hfh.api.skill.model;

import java.util.Map;

public class Skill {

    private String name;
    private String description;
    private String version;
    private String author;
    private String category;
    private String promptTemplate;
    private Map<String, String> parameters;
    private SkillApiConfig apiConfig;
    private String basePath;

    public Skill() {
    }

    public Skill(String name, String description, String version, String author,
                 String category, String promptTemplate, Map<String, String> parameters, String basePath) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.author = author;
        this.category = category;
        this.promptTemplate = promptTemplate;
        this.parameters = parameters;
        this.basePath = basePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPromptTemplate() {
        return promptTemplate;
    }

    public void setPromptTemplate(String promptTemplate) {
        this.promptTemplate = promptTemplate;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public SkillApiConfig getApiConfig() {
        return apiConfig;
    }

    public void setApiConfig(SkillApiConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    @Override
    public String toString() {
        return "Skill{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", version='" + version + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", parameters=" + parameters +
                ", apiConfig=" + apiConfig +
                ", basePath='" + basePath + '\'' +
                '}';
    }
}
