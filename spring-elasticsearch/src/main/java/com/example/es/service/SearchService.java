package com.example.es.service;

import com.example.es.dto.SearchRequest;
import com.example.es.dto.SearchResponse;
import com.example.es.entity.es.BookEs;
import com.example.es.entity.pg.BookPg;
import com.example.es.repository.es.BookEsRepository;
import com.example.es.repository.pg.BookPgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {

    private final BookEsRepository bookEsRepository;
    private final BookPgRepository bookPgRepository;
    private final ElasticsearchRestTemplate elasticsearchRestTemplate;

    /**
     * 策略1：优先从 ES 搜索，失败时降级到 PostgreSQL
     */
    public SearchResponse searchWithFallback(SearchRequest request) {
        try {
            // 使用 ElasticsearchRestTemplate 查询
            Page<BookEs> esResult = searchFromEsWithTemplate(request);

            if (esResult != null && esResult.hasContent()) {
                log.info("从 ES 返回结果，总数：{}", esResult.getTotalElements());
                return fromEsPage(esResult);
            }

            log.info("ES 无结果，降级到 PostgreSQL，关键词：{}", request.getKeyword());
            return fromPgPage(searchFromPg(request));

        } catch (Exception e) {
            log.error("ES 异常，降级到 PostgreSQL", e);
            return fromPgPage(searchFromPg(request));
        }
    }

    /**
     * 只从 ES 搜索
     */
    public SearchResponse searchFromEsOnly(SearchRequest request) {
        try {
            Page<BookEs> esResult = searchFromEsWithTemplate(request);
            return fromEsPage(esResult);
        } catch (Exception e) {
            log.error("ES 搜索失败", e);
            SearchResponse response = new SearchResponse();
            response.setTotal(0);
            response.setFromSource("elasticsearch(error)");
            response.setData(List.of());
            return response;
        }
    }

    /**
     * 只从 PostgreSQL 搜索
     */
    public SearchResponse searchFromPgOnly(SearchRequest request) {
        Page<BookPg> pgResult = searchFromPg(request);
        return fromPgPage(pgResult);
    }

    /**
     * 使用 ElasticsearchRestTemplate 进行全文搜索
     */
    private Page<BookEs> searchFromEsWithTemplate(SearchRequest request) {
        if (request.getKeyword() == null || request.getKeyword().isEmpty()) {
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
            return bookEsRepository.findAll(pageable);
        }

        log.info("ES 全文搜索，关键词: {}", request.getKeyword());

        // 构建多字段匹配查询（搜索 title 和 author 字段）
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(request.getKeyword(), "title", "author"))
                .withPageable(PageRequest.of(request.getPage(), request.getSize()));

        SearchHits<BookEs> searchHits = elasticsearchRestTemplate.search(queryBuilder.build(), BookEs.class);

        List<BookEs> content = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());

        log.info("ES 搜索到 {} 条结果", content.size());

        return new PageImpl<>(content, PageRequest.of(request.getPage(), request.getSize()), searchHits.getTotalHits());
    }

    /**
     * 从 PostgreSQL 搜索
     */
    private Page<BookPg> searchFromPg(SearchRequest request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());

        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            log.info("PG 搜索，关键词: {}", request.getKeyword());
            return bookPgRepository.findByTitleContaining(request.getKeyword(), pageable);
        }

        return bookPgRepository.findAll(pageable);
    }

    /**
     * 强制同步所有数据到 ES
     */
    public void forceSyncToEs() {
        log.info("强制同步所有数据到 ES...");

        List<BookPg> allBooks = bookPgRepository.findAll();
        if (allBooks.isEmpty()) {
            log.warn("PostgreSQL 中没有数据");
            return;
        }

        // 删除 ES 中现有的所有数据
        bookEsRepository.deleteAll();
        log.info("已清空 ES 中的旧数据");

        // 转换并同步
        List<BookEs> esBooks = allBooks.stream()
                .map(this::convertToEs)
                .collect(Collectors.toList());

        bookEsRepository.saveAll(esBooks);
        log.info("强制同步完成，同步 {} 条数据到 ES", esBooks.size());
    }

    /**
     * 获取 ES 中的数据量
     */
    public long getEsCount() {
        return bookEsRepository.count();
    }

    /**
     * 数据转换：PostgreSQL 实体 -> Elasticsearch 实体
     */
    private BookEs convertToEs(BookPg book) {
        BookEs bookEs = new BookEs();
        bookEs.setId(String.valueOf(book.getId()));
        bookEs.setTitle(book.getTitle());
        bookEs.setAuthor(book.getAuthor());
        bookEs.setCategory(book.getCategory());
        bookEs.setPrice(book.getPrice());
        bookEs.setStock(book.getStock());
        bookEs.setDescription(book.getDescription());
        bookEs.setPublishDate(book.getPublishDate() != null ? book.getPublishDate().toString() : null);
        bookEs.setUpdatedAt(new java.util.Date().toString());
        return bookEs;
    }

    /**
     * 转换 ES 结果为响应
     */
    private SearchResponse fromEsPage(Page<BookEs> page) {
        SearchResponse response = new SearchResponse();
        response.setTotal(page.getTotalElements());
        response.setData(page.getContent().stream()
                .map(this::convertToMap)
                .collect(Collectors.toList()));
        response.setFromSource("elasticsearch");
        return response;
    }

    /**
     * 转换 PG 结果为响应
     */
    private SearchResponse fromPgPage(Page<BookPg> page) {
        SearchResponse response = new SearchResponse();
        response.setTotal(page.getTotalElements());
        response.setData(page.getContent().stream()
                .map(this::convertToMap)
                .collect(Collectors.toList()));
        response.setFromSource("postgresql");
        return response;
    }

    /**
     * 转换 BookEs 为 Map
     */
    private Map<String, Object> convertToMap(BookEs book) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", book.getId());
        map.put("title", book.getTitle());
        map.put("author", book.getAuthor());
        map.put("category", book.getCategory());
        map.put("price", book.getPrice());
        map.put("stock", book.getStock());
        map.put("description", book.getDescription());
        map.put("publishDate", book.getPublishDate());
        return map;
    }

    /**
     * 转换 BookPg 为 Map
     */
    private Map<String, Object> convertToMap(BookPg book) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", book.getId());
        map.put("title", book.getTitle());
        map.put("author", book.getAuthor());
        map.put("category", book.getCategory());
        map.put("price", book.getPrice());
        map.put("stock", book.getStock());
        map.put("description", book.getDescription());
        map.put("publishDate", book.getPublishDate());
        return map;
    }
}