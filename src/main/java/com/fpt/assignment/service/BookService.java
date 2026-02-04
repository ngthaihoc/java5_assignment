package com.fpt.assignment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fpt.assignment.entity.Book;
import com.fpt.assignment.entity.Category;
import com.fpt.assignment.repository.BookRepository;
import com.fpt.assignment.repository.CategoryRepository;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    CategoryRepository categoryRepository;

    public Optional<Book> getBookDetail(Long id) {
        return bookRepository.findById(id)
                .map(book -> {
                    // Calculate oldPrice
                    book.setOldPrice(book.getPrice().multiply(new java.math.BigDecimal("1.25")));
                    return book;
                });
    }

    public Page<Book> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public List<Book> findRelatedBooks(Long categoryId, Long bookId) {
        Pageable pageable = PageRequest.of(0, 4);

        System.out.println("Finding related books for categoryId: " + categoryId + ", bookId: " + bookId);
        return bookRepository
                .findRelatedBooks(categoryId, bookId, pageable)
                .getContent();
    }

    public Page<Book> searchBooks(String keyword, Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            return bookRepository.findAll(pageable);
        }
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorNameContainingIgnoreCase(keyword, keyword,
                pageable);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    public List<Category> getAllCategories() {

        return categoryRepository.findAll();
    }

    public Page<Book> searchBooksWithFilter(
            String keyword,
            Long categoryId,
            Integer minPrice,
            Integer maxPrice,
            Pageable pageable) {
        if (keyword == null || keyword.isEmpty()) {
            keyword = "";
        }
        return bookRepository.searchBooksWithFilter(
                keyword,
                categoryId,
                minPrice,
                maxPrice,
                pageable);
    }
}
