package com.fpt.assignment.repository;

import com.fpt.assignment.entity.Book;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Override
    List<Book> findAll();

    @Query("SELECT b FROM Book b WHERE b.category.id = ?1")
    List<Book> findByCategoryId(Long cateid, PageRequest of);

    List<Book> findAllByOrderByPublishDateDesc(PageRequest of);
}
