package com.fpt.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fpt.assignment.entity.Book;
import com.fpt.assignment.entity.Category;
import com.fpt.assignment.service.BookService;

@Controller
public class SearchController {

    @Autowired
    private BookService bookService;

    @GetMapping("/search")
    public String listAllBooks(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "15") int size,
            @RequestParam(name = "keyword", defaultValue = "") String keyword,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(required = false) String sort,
            Model model) {

        if (page < 1)
            page = 1;

        Sort sortObj = Sort.unsorted();

        if ("az".equals(sort))
            sortObj = Sort.by("title").ascending();
        else if ("za".equals(sort))
            sortObj = Sort.by("title").descending();
        else if ("priceAsc".equals(sort))
            sortObj = Sort.by("price").ascending();
        else if ("priceDesc".equals(sort))
            sortObj = Sort.by("price").descending();
        else if ("newest".equals(sort))
            sortObj = Sort.by("createdDate").descending();

        Pageable pageable = PageRequest.of(page - 1, size, sortObj);

        Page<Book> bookPage = bookService.searchBooksWithFilter(
                keyword,
                category,
                minPrice,
                maxPrice,
                pageable);

        Page<Book> allBooksPage = null;
        if (bookPage.isEmpty()) {
            allBooksPage = bookService.getAllBooks(pageable);
        }

        List<Category> categories = bookService.getAllCategories();

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("allBooksPage", allBooksPage);
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        model.addAttribute("selectedCategory", category);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("sort", sort);

        return "views/book/search";
    }

}