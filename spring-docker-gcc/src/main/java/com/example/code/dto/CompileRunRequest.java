package com.example.code.dto;

import java.util.List;

public class CompileRunRequest {
    private String code;
    private List<String> args;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getArgs() {
        return args;
    }

    public void setArgs(List<String> args) {
        this.args = args;
    }
}
