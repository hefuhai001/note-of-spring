package com.example.es.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchResponse {
    private long total;
    private String fromSource;
    private List<Map<String, Object>> data;
}