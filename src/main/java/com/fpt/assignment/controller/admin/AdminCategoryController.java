package com.fpt.assignment.controller.admin;

import com.fpt.assignment.entity.Category;
import com.fpt.assignment.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    final CategoryRepository categoryRepository;

    @GetMapping
    public String categories(Model model) {
        model.addAttribute("active", "categories");
        model.addAttribute("categories", categoryRepository.findAll());
        return "views/admin/categories";
    }


    @PostMapping("/create")
    public String createBook(@ModelAttribute Category cate) {
        categoryRepository.save(cate);
        return "redirect:/admin/categories";
    }

    @PostMapping("/update")
    public String updateBook(@ModelAttribute Category cate) {
        categoryRepository.save(cate);
        return "redirect:/admin/categories";
    }


    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        }
        return "redirect:/admin/categories";
    }


}
