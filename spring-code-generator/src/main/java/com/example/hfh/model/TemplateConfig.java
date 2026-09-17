package com.example.hfh.model;

import java.util.List;

public class TemplateConfig {
    private String templateName;
    private String description;
    private List<ParameterInfo> parameters;

    public TemplateConfig() {
    }

    public TemplateConfig(String templateName, String description, List<ParameterInfo> parameters) {
        this.templateName = templateName;
        this.description = description;
        this.parameters = parameters;
    }

    // Getters and Setters
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<ParameterInfo> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParameterInfo> parameters) {
        this.parameters = parameters;
    }

    public static class ParameterInfo {
        private String name;
        private String type;
        private String description;
        private Boolean required;

        public ParameterInfo() {
        }

        public ParameterInfo(String name, String type, String description, Boolean required) {
            this.name = name;
            this.type = type;
            this.description = description;
            this.required = required;
        }

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Boolean getRequired() {
            return required;
        }

        public void setRequired(Boolean required) {
            this.required = required;
        }
    }
}