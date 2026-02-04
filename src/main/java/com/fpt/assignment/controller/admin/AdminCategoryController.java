package com.fpt.assignment.controller.admin;

import com.fpt.assignment.entity.Category;
import com.fpt.assignment.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String createCategory(@ModelAttribute Category cate, RedirectAttributes redirectAttributes) {
        try {
            categoryRepository.save(cate);
            redirectAttributes.addFlashAttribute("success", "Thêm danh mục thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @PostMapping("/update")
    public String updateCategory(@ModelAttribute Category cate, RedirectAttributes redirectAttributes) {
        try {
            categoryRepository.save(cate);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi cập nhật: " + e.getMessage());
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            if (categoryRepository.existsById(id)) {
                categoryRepository.deleteById(id);
                redirectAttributes.addFlashAttribute("success", "Xóa danh mục thành công!");
            } else {
                redirectAttributes.addFlashAttribute("error", "Danh mục không tồn tại!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Không thể xóa danh mục này vì đang chứa sách hoặc dữ liệu liên quan!");
        }
        return "redirect:/admin/categories";
    }

}
