package com.example.hfh.bo;

import java.util.Map;

public class CodeGenerationRequest {
    private String templateName;
    private Map<String, Object> variables;

    // 构造函数
    public CodeGenerationRequest() {
    }

    public CodeGenerationRequest(String templateName, Map<String, Object> variables) {
        this.templateName = templateName;
        this.variables = variables;
    }

    // Getter和Setter
    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }
}