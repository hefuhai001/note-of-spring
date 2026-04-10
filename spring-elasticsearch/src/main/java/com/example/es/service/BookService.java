package com.example.es.service;

import com.example.es.entity.pg.BookPg;
import com.example.es.repository.pg.BookPgRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

    private final BookPgRepository bookPgRepository;

    @Transactional
    public BookPg save(BookPg book) {
        return bookPgRepository.save(book);
    }

    @Transactional
    public List<BookPg> saveAll(List<BookPg> books) {
        return bookPgRepository.saveAll(books);
    }

    public Optional<BookPg> findById(Long id) {
        return bookPgRepository.findById(id);
    }

    public List<BookPg> findAll() {
        return bookPgRepository.findAll();
    }

    public Page<BookPg> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return bookPgRepository.findAll(pageable);
    }

    @Transactional
    public void deleteById(Long id) {
        bookPgRepository.deleteById(id);
    }

    public Page<BookPg> searchByKeyword(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return bookPgRepository.findByTitleContaining(keyword, pageable);
    }
}