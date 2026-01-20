package com.fpt.assignment.repository;

import com.fpt.assignment.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Override
    List<Book> findAll();
}
