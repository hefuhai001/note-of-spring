package com.hfh.api.skill.executor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hfh.api.skill.model.SkillApiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class SkillApiExecutor {

    private static final Logger log = LoggerFactory.getLogger(SkillApiExecutor.class);

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public SkillApiExecutor() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    public String execute(SkillApiConfig apiConfig) {
        if (apiConfig == null || apiConfig.getUrl() == null) {
            log.warn("API config or URL is null, skipping API call");
            return null;
        }

        try {
            log.info("Calling API: {} {}", apiConfig.getMethod(), apiConfig.getUrl());

            HttpHeaders headers = new HttpHeaders();
            if (apiConfig.getHeaders() != null) {
                apiConfig.getHeaders().forEach(headers::set);
            }
            if (!headers.containsKey("Content-Type")) {
                headers.set("Content-Type", "application/json");
            }

            HttpEntity<String> entity = new HttpEntity<>(apiConfig.getBody(), headers);
            HttpMethod method = HttpMethod.valueOf(apiConfig.getMethod() != null ? apiConfig.getMethod().toUpperCase() : "GET");

            ResponseEntity<String> response = restTemplate.exchange(
                    apiConfig.getUrl(),
                    method,
                    entity,
                    String.class
            );

            String responseBody = response.getBody();
            log.debug("API response status: {}", response.getStatusCode());

            if (apiConfig.getResponseKey() != null && responseBody != null) {
                return extractValue(responseBody, apiConfig.getResponseKey());
            }

            return responseBody;
        } catch (RestClientException e) {
            log.error("API call failed: {} {}", apiConfig.getMethod(), apiConfig.getUrl(), e);
            return null;
        }
    }

    private String extractValue(String json, String keyPath) {
        try {
            JsonNode node = objectMapper.readTree(json);
            String[] keys = keyPath.split("\\.");
            for (String key : keys) {
                if (node.isArray()) {
                    node = node.get(Integer.parseInt(key));
                } else {
                    node = node.get(key);
                }
                if (node == null) {
                    log.warn("Key path '{}' not found in response", keyPath);
                    return json;
                }
            }
            if (node.isTextual()) {
                return node.asText();
            }
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(node);
        } catch (Exception e) {
            log.warn("Failed to extract key '{}' from response, returning raw response", keyPath, e);
            return json;
        }
    }
}
