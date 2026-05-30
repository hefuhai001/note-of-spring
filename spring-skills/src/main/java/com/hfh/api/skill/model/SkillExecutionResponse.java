package com.hfh.api.skill.model;

public class SkillExecutionResponse {

    private String skillName;
    private String result;
    private boolean success;
    private String errorMessage;

    public SkillExecutionResponse() {
    }

    public static SkillExecutionResponse success(String skillName, String result) {
        SkillExecutionResponse response = new SkillExecutionResponse();
        response.setSkillName(skillName);
        response.setResult(result);
        response.setSuccess(true);
        return response;
    }

    public static SkillExecutionResponse failure(String skillName, String errorMessage) {
        SkillExecutionResponse response = new SkillExecutionResponse();
        response.setSkillName(skillName);
        response.setErrorMessage(errorMessage);
        response.setSuccess(false);
        return response;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
