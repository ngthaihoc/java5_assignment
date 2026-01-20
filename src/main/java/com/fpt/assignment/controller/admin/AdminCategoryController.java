package com.fpt.assignment.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    @GetMapping
    public String categories(Model model) {

        model.addAttribute("active", "categories");

        model.addAttribute("categories", List.of(
                Map.of("id", "SELF", "name", "Phát triển bản thân (Self-Help)"),
                Map.of("id", "PSYC", "name", "Tâm lý học (Psychology)"),
                Map.of("id", "BIOG", "name", "Tiểu sử - Hồi ký (Biography)"),
                Map.of("id", "PHIL", "name", "Triết học (Philosophy)")
        ));

        return "views/admin/categories";
    }
}
