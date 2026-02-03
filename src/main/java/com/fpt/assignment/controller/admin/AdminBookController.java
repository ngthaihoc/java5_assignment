package com.fpt.assignment.controller.admin;

import com.fpt.assignment.entity.Book;
import com.fpt.assignment.repository.BookRepository;
import com.fpt.assignment.repository.CategoryRepository;
import com.fpt.assignment.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class AdminBookController {
    final BookRepository bookRepository;
    final CategoryRepository categoryRepository;
    final PublisherRepository publisherRepository;

    @GetMapping
    public String books(Model model) {
        model.addAttribute("active", "books");
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("publishers", publisherRepository.findAll());
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

    @GetMapping("/api/{id}")
    @ResponseBody
    public Book getBook(@PathVariable("id") Long id) {
        return bookRepository.findById(id).orElse(null);
    }
}