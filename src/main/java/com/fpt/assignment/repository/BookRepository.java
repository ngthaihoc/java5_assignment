package com.fpt.assignment.repository;

import com.fpt.assignment.entity.Book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b WHERE b.category.id = :categoryId AND b.id <> :bookId ORDER BY RAND() LIMIT 4")
    List<Book> findRelatedBooks(String categoryId, Long bookId);
}
