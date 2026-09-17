package com.example.hfh.controller;

import com.example.hfh.entity.pg.BookPg;
import com.example.hfh.service.BookService;
import com.example.hfh.service.SyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final SyncService syncService;

    @PostMapping
    public ResponseEntity<BookPg> create(@RequestBody BookPg book) {
        BookPg saved = bookService.save(book);
        syncService.syncSingleToEs(saved);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/batch")
    public ResponseEntity<List<BookPg>> createBatch(@RequestBody List<BookPg> books) {
        List<BookPg> saved = bookService.saveAll(books);
        syncService.syncToEs(saved);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookPg> getById(@PathVariable Long id) {
        return bookService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<BookPg>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(bookService.findAll(page, size));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.deleteById(id);
        syncService.deleteFromEs(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/sync")
    public ResponseEntity<String> syncToEs() {
        syncService.forceSync();
        return ResponseEntity.ok("同步完成");
    }

}