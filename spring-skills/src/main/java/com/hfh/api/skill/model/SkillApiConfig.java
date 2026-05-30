package com.hfh.api.skill.model;

import java.util.Map;

public class SkillApiConfig {

    private String url;
    private String method;
    private Map<String, String> headers;
    private String body;
    private String responseKey;

    public SkillApiConfig() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getResponseKey() {
        return responseKey;
    }

    public void setResponseKey(String responseKey) {
        this.responseKey = responseKey;
    }

    @Override
    public String toString() {
        return "SkillApiConfig{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", headers=" + headers +
                ", responseKey='" + responseKey + '\'' +
                '}';
    }
}
