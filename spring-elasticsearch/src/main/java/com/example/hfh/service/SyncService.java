package com.example.hfh.service;

import com.example.hfh.entity.es.BookEs;
import com.example.hfh.entity.pg.BookPg;
import com.example.hfh.repository.es.BookEsRepository;
import com.example.hfh.repository.pg.BookPgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncService {

    private final BookPgRepository bookPgRepository;
    private final BookEsRepository bookEsRepository;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void initSync() {
        log.info("开始初始化同步数据到 Elasticsearch...");
        List<BookPg> allBooks = bookPgRepository.findAll();
        if (!allBooks.isEmpty()) {
            syncToEs(allBooks);
        }
        log.info("初始化同步完成，共同步 {} 条数据", allBooks.size());
    }

    public void syncToEs(List<BookPg> books) {
        List<BookEs> esBooks = books.stream()
                .map(this::convertToEs)
                .collect(Collectors.toList());
        bookEsRepository.saveAll(esBooks);
        log.info("同步 {} 条数据到 ES", esBooks.size());
    }

    public void syncSingleToEs(BookPg book) {
        BookEs bookEs = convertToEs(book);
        bookEsRepository.save(bookEs);
        log.info("同步单条数据到 ES: {}", book.getTitle());
    }

    public void deleteFromEs(Long id) {
        bookEsRepository.deleteById(String.valueOf(id));
        log.info("从 ES 删除数据: {}", id);
    }

    private BookEs convertToEs(BookPg book) {
        BookEs bookEs = new BookEs();
        bookEs.setId(String.valueOf(book.getId()));
        bookEs.setTitle(book.getTitle());
        bookEs.setAuthor(book.getAuthor());
        bookEs.setCategory(book.getCategory());
        bookEs.setPrice(book.getPrice());
        bookEs.setStock(book.getStock());
        bookEs.setDescription(book.getDescription());

        // 格式化日期为字符串
        bookEs.setPublishDate(formatDate(book.getPublishDate()));
        bookEs.setUpdatedAt(formatDate(new Date()));

        return bookEs;
    }

    private String formatDate(Date date) {
        if (date == null) {
            return null;
        }
        return DATE_FORMAT.format(date);
    }

    // 添加强制同步方法
    public void forceSync() {
        log.info("强制同步所有数据到 ES...");
        List<BookPg> allBooks = bookPgRepository.findAll();
        if (!allBooks.isEmpty()) {
            // 先删除现有索引
            bookEsRepository.deleteAll();
            // 重新同步
            syncToEs(allBooks);
            log.info("强制同步完成，同步 {} 条数据", allBooks.size());
        }
    }
}