package com.example.hfh.repository.es;

import com.example.hfh.entity.es.BookEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface BookEsRepository extends ElasticsearchRepository<BookEs, String> {

    Page<BookEs> findByTitleContainingOrAuthorContaining(String title, String author, Pageable pageable);

    // 方法1：使用 @Query 注解自定义查询
    @Query("{\"bool\": {\"should\": [{\"match\": {\"title\": \"?0\"}}, {\"match\": {\"author\": \"?0\"}}]}}")
    Page<BookEs> searchByKeyword(String keyword, Pageable pageable);

    // 方法2：使用原生查询
    @Query("{\"match\": {\"title\": \"?0\"}}")
    Page<BookEs> searchByTitle(String keyword, Pageable pageable);
}