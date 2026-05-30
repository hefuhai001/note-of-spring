package com.hfh.api.skill.model;

import java.util.Map;

public class SkillExecutionRequest {

    private String skillName;
    private Map<String, String> variables;
    private String userInput;

    public SkillExecutionRequest() {
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }
}
