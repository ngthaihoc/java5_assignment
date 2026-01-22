package com.fpt.assignment.repository;

import com.fpt.assignment.entity.Book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Override
    List<Book> findAll();

    @Query("SELECT b FROM Book b WHERE b.category.id = ?1")
    List<Book> findByCategoryId(Long cateid, PageRequest of);

    @Query("SELECT b FROM Book b WHERE b.category.id = :categoryId AND b.id <> :bookId ORDER BY RAND() LIMIT 4")
    List<Book> findRelatedBooks(@Param("categoryId") String categoryId, @Param("bookId") Long bookId);

    Page<Book> findByTitleContainingIgnoreCaseOrAuthorNameContainingIgnoreCase(String keyword1, String keyword2, Pageable pageable);
}
