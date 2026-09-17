package com.example.hfh.dto;

import java.util.List;

public class RunRequest {
    private String taskId;
    private List<String> args;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }
}
