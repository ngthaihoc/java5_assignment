package com.fpt.assignment.controller.admin;

import com.fpt.assignment.entity.Book;
import com.fpt.assignment.repository.BookRepository;
import com.fpt.assignment.repository.CategoryRepository;
import com.fpt.assignment.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String createBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        try {
            bookRepository.save(book);
            redirectAttributes.addFlashAttribute("success", "Thêm sách thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @PostMapping("/update")
    public String updateBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
        try {
            bookRepository.save(book);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi cập nhật: " + e.getMessage());
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            if (bookRepository.existsById(id)) {
                bookRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Xóa thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Sách không tồn tại!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Không thể xóa sách này (có thể đang có đơn hàng liên quan).");
        }
        return "redirect:/admin/books";
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Book getBook(@PathVariable("id") Long id) {
        return bookRepository.findById(id).orElse(null);
    }
}