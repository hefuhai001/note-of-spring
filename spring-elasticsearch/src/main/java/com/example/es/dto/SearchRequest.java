package com.example.es.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SearchRequest {
    private String keyword;
    private String category;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private int page = 0;
    private int size = 10;
}