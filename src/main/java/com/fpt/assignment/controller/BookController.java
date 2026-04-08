package com.fpt.assignment.controller;

import com.fpt.assignment.entity.Book;
import com.fpt.assignment.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable long id) {
        Book book = bookService.findById(id);
        return ResponseEntity.ok().body(book);
    }

    @GetMapping("/top")
    public ResponseEntity<Page<Book>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {

        Page<Book> list = bookService.getAllBooks(PageRequest.of(page, size));
        return ResponseEntity.ok(list);
    }

    @GetMapping("/newest")
    public ResponseEntity<Page<Book>> getNewestBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size
    ) {
        Page<Book> list = bookService.getNewestBooks(PageRequest.of(page, size));
        return ResponseEntity.ok(list);
    }

    @GetMapping("/by-category")
    public ResponseEntity<Page<Book>> getBooksByCategory(
            @RequestParam(value = "categoryId", required = false) Long cateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        Page<Book> books;
        if (cateId == null) {
            // Nếu không có cateId, lấy tất cả sách (tương ứng nút "Tất cả" ở Vue)
            books = bookService.getAllBooks(PageRequest.of(page, size));
        } else {
            // Nếu có cateId, lọc theo thể loại
            books = bookService.getBooksByCategoryId(cateId,  PageRequest.of(page, size));
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Book>> searchBooks(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(required = false) Integer category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "newest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Sort sortOrder = Sort.by("id").descending();
        if (sort.equals("priceAsc")) {
            sortOrder = Sort.by("price").ascending();
        } else if (sort.equals("priceDesc")) {
            sortOrder = Sort.by("price").descending();
        } else if (sort.equals("az")) {
            sortOrder = Sort.by("title").ascending();
        } else if (sort.equals("za")) {
            sortOrder = Sort.by("title").descending();
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<Book> list = bookService.searchBooksWithFilter(keyword, category, minPrice, maxPrice, pageable);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/related")
    public ResponseEntity<List<Book>> getRelatedBooks(@RequestParam Long categoryId, @RequestParam Long bookId) {
        List<Book> relatedBooks = bookService.findRelatedBooks(categoryId, bookId);

        return ResponseEntity.ok(relatedBooks);
    }
}
