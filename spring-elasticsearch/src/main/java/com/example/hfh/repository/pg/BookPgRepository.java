package com.example.hfh.repository.pg;

import com.example.hfh.entity.pg.BookPg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface BookPgRepository extends JpaRepository<BookPg, Long> {

    Page<BookPg> findByTitleContaining(String keyword, Pageable pageable);

    @Query("SELECT b FROM BookPg b WHERE " +
            "(:keyword IS NULL OR b.title LIKE %:keyword% OR b.author LIKE %:keyword%) " +
            "AND (:category IS NULL OR b.category = :category) " +
            "AND (:minPrice IS NULL OR b.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR b.price <= :maxPrice)")
    Page<BookPg> complexSearch(@Param("keyword") String keyword,
                               @Param("category") String category,
                               @Param("minPrice") BigDecimal minPrice,
                               @Param("maxPrice") BigDecimal maxPrice,
                               Pageable pageable);
}