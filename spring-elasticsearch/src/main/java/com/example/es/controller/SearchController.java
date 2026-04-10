package com.example.es.controller;

import com.example.es.dto.SearchRequest;
import com.example.es.dto.SearchResponse;
import com.example.es.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    /**
     * 搜索接口 - 优先 ES，失败降级到 PostgreSQL
     */
    @PostMapping("/fallback")
    public SearchResponse searchWithFallback(@RequestBody SearchRequest request) {
        log.info("搜索请求: keyword={}, page={}, size={}",
                request.getKeyword(), request.getPage(), request.getSize());
        return searchService.searchWithFallback(request);
    }

    /**
     * 只从 ES 搜索
     */
    @PostMapping("/es")
    public SearchResponse searchFromEs(@RequestBody SearchRequest request) {
        log.info("ES 搜索请求: keyword={}", request.getKeyword());
        return searchService.searchFromEsOnly(request);
    }

    /**
     * 只从 PostgreSQL 搜索
     */
    @PostMapping("/pg")
    public SearchResponse searchFromPg(@RequestBody SearchRequest request) {
        log.info("PG 搜索请求: keyword={}", request.getKeyword());
        return searchService.searchFromPgOnly(request);
    }

    /**
     * 强制同步数据到 ES
     */
    @PostMapping("/sync")
    public String syncToEs() {
        log.info("手动触发同步到 ES");
        searchService.forceSyncToEs();
        return "数据已同步到 Elasticsearch";
    }

    /**
     * 查看 ES 中的数据量
     */
    @GetMapping("/count")
    public long getEsCount() {
        return searchService.getEsCount();
    }
}