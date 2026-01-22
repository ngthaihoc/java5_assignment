package com.fpt.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.fpt.assignment.entity.Book;
import com.fpt.assignment.service.BookService;

@Controller
public class BookDetailController {
    @Autowired
    private BookService bookService;

    @GetMapping("/book/{id}")
    public String bookDetail(@PathVariable Long id, Model model) {

        Book book = bookService.findById(id);

        if (book == null) {
            model.addAttribute("book", null);
            return "views/product/productDetail";
        }

        model.addAttribute("book", book);
        
        // nếu chưa làm relatedBooks thì cho list rỗng
        model.addAttribute("relatedBooks", List.of());

        return "views/product/productDetail";
    }
}
