package com.fpt.assignment.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fpt.assignment.entity.Book;
import com.fpt.assignment.service.BookService;

@Controller
public class SearchController {

    @Autowired
    private BookService bookService;

    @GetMapping("/search")
    public String listAllBooks(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "15") int size,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            Model model) {

        if (page < 1) page = 1;

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Book> bookPage = bookService.searchBooks(keyword, pageable);

        // Nếu không có kết quả tìm kiếm, hiển thị tất cả sách
        Page<Book> allBooksPage = null;

        if (bookPage.isEmpty()) {
            allBooksPage = bookService.getAllBooks(pageable);
        }

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("allBooksPage", allBooksPage);

        return "views/book/search";
    }

}