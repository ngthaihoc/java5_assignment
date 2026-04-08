package com.fpt.assignment.controller.admin;

import com.fpt.assignment.entity.Book;
import com.fpt.assignment.repository.BookRepository;
import com.fpt.assignment.repository.CategoryRepository;
import com.fpt.assignment.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/books")
@RequiredArgsConstructor
public class AdminBookRestController {

    final BookRepository bookRepository;
    final CategoryRepository categoryRepository;
    final PublisherRepository publisherRepository;

    @GetMapping
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable Long id) {
        return bookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(bookRepository.save(mapToBook(body, new Book())));
    }

    @PutMapping
    public ResponseEntity<?> updateBook(@RequestBody Map<String, Object> body) {
        Long id = Long.parseLong(body.get("id").toString());
        Book book = bookRepository.findById(id).orElse(new Book());
        return ResponseEntity.ok(bookRepository.save(mapToBook(body, book)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        bookRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Xóa thành công"));
    }

    private Book mapToBook(Map<String, Object> body, Book book) {
        if (body.containsKey("title")) book.setTitle(body.get("title").toString());
        if (body.containsKey("authorName")) book.setAuthorName(body.get("authorName").toString());
        if (body.containsKey("price")) book.setPrice(new BigDecimal(body.get("price").toString()));
        if (body.containsKey("quantity")) book.setQuantity(Integer.parseInt(body.get("quantity").toString()));
        if (body.containsKey("description")) book.setDescription(body.get("description").toString());
        if (body.containsKey("image") && body.get("image") != null && !body.get("image").toString().isBlank())
            book.setImage(body.get("image").toString());
        if (body.containsKey("translator")) book.setTranslator(body.get("translator").toString());
        if (body.containsKey("dimensions")) book.setDimensions(body.get("dimensions").toString());
        if (body.containsKey("coverType")) book.setCoverType(body.get("coverType").toString());
        if (body.containsKey("pageCount") && body.get("pageCount") != null)
            book.setPageCount(Integer.parseInt(body.get("pageCount").toString()));
        if (body.containsKey("available"))
            book.setAvailable(Boolean.parseBoolean(body.get("available").toString()));
        if (body.containsKey("category.id") && body.get("category.id") != null)
            categoryRepository.findById(Long.parseLong(body.get("category.id").toString()))
                    .ifPresent(book::setCategory);
        if (body.containsKey("publisher.id") && body.get("publisher.id") != null)
            publisherRepository.findById(Long.parseLong(body.get("publisher.id").toString()))
                    .ifPresent(book::setPublisher);
        return book;
    }
}
