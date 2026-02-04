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

        Book book = bookService.getBookDetail(id).orElse(null);

        if (book == null) {
            model.addAttribute("book", null);
            return "views/book/bookdetail";
        }

        if (book.getCategory() != null) {
            List<Book> relatedBooks = bookService.findRelatedBooks(book.getCategory().getId(), book.getId());
            model.addAttribute("relatedBooks", relatedBooks);
            System.out.println("Related books found: " + relatedBooks.size());
        } else {
            model.addAttribute("relatedBooks", List.of());
        }

        model.addAttribute("book", book);

        return "views/book/bookdetail";
    }

}
