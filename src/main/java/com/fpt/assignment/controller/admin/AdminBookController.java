package com.fpt.assignment.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/books")
public class AdminBookController {

    // HIỂN THỊ DANH SÁCH SÁCH
    @GetMapping
    public String books(Model model) {

        model.addAttribute("active", "books");

        // DATA MẪU
        model.addAttribute("books", List.of(
                Map.of(
                        "id", 1,
                        "title", "Atomic Habits",
                        "author", "James Clear",
                        "price", 180000,
                        "quantity", 200,
                        "image", "/uploads/atomic_habits.jpg"
                ),
                Map.of(
                        "id", 2,
                        "title", "Meditations",
                        "author", "Marcus Aurelius",
                        "price", 105000,
                        "quantity", 150,
                        "image", "/uploads/meditations.jpg"
                )
        ));

        return "views/admin/books";
    }

    // THÊM SÁCH
    @PostMapping("/create")
    public String createBook(
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam(required = false) String image,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String description
    ) {

        System.out.println("CREATE BOOK:");
        System.out.println(title + " - " + author + " - " + image);

        return "redirect:/admin/books";
    }

    // CẬP NHẬT SÁCH
    @PostMapping("/update")
    public String updateBook(
            @RequestParam Long id,
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam(required = false) String image,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String description
    ) {

        // 🔹 Sau này update DB
        System.out.println("UPDATE BOOK ID = " + id);

        return "redirect:/admin/books";
    }
}
