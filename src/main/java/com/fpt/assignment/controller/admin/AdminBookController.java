package com.fpt.assignment.controller.admin;

import com.fpt.assignment.entity.Book;
import com.fpt.assignment.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class AdminBookController {
    final BookRepository bookRepository;


    @GetMapping
    public String books(Model model) {
        model.addAttribute("active", "books");
        model.addAttribute("books", bookRepository.findAll());
        return "views/admin/books";
    }

    @PostMapping("/create")
    public String createBook(@ModelAttribute Book book) {
        bookRepository.save(book);
        return "redirect:/admin/books";
    }

    @PostMapping("/update")
    public String updateBook(@ModelAttribute Book book) {
        bookRepository.save(book);
        return "redirect:/admin/books";
    }


    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        }
        return "redirect:/admin/books";
    }
}