package com.fpt.assignment.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@Controller
public class AdminCustomerController {

    @GetMapping("/admin/customers")
    public String customers(Model model) {

        model.addAttribute("active", "customers");

        model.addAttribute("customers", List.of(
                Map.of(
                        "id", 1,
                        "name", "Nguyễn Văn An",
                        "email", "nguyenvanan@email.com",
                        "phone", "0901234567",
                        "rank", "Vàng",
                        "orders", 12,
                        "lastOrder", "10/01/2026",
                        "total", 2450000,
                        "status", true
                ),
                Map.of(
                        "id", 2,
                        "name", "Trần Thị Bình",
                        "email", "tranthibinh@email.com",
                        "phone", "0912345678",
                        "rank", "Bạc",
                        "orders", 8,
                        "lastOrder", "08/01/2026",
                        "total", 1680000,
                        "status", true
                ),
                Map.of(
                        "id", 5,
                        "name", "Hoàng Văn Em",
                        "email", "hoangvanem@email.com",
                        "phone", "0945678901",
                        "rank", "Thường",
                        "orders", 3,
                        "lastOrder", "15/11/2025",
                        "total", 450000,
                        "status", false
                )
        ));

        return "views/admin/customers";
    }
}
