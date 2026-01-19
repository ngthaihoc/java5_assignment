package com.fpt.assignment.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @GetMapping
    public String orders(Model model) {

        model.addAttribute("active", "orders");

        model.addAttribute("orders", List.of(
                Map.of(
                        "id", 1234,
                        "customer", "Nguyễn Văn A",
                        "email", "nguyenvana@email.com",
                        "date", "10/01/2026",
                        "items", 3,
                        "total", 247000,
                        "status", 1   // 1: Đang giao
                ),
                Map.of(
                        "id", 1235,
                        "customer", "Trần Thị B",
                        "email", "tranthib@email.com",
                        "date", "09/01/2026",
                        "items", 2,
                        "total", 168000,
                        "status", 2   // 2: Đã giao
                ),
                Map.of(
                        "id", 1236,
                        "customer", "Lê Văn C",
                        "email", "levanc@email.com",
                        "date", "12/01/2026",
                        "items", 1,
                        "total", 79000,
                        "status", 0   // 0: Đang xử lý
                ),
                Map.of(
                        "id", 1239,
                        "customer", "Vũ Thị F",
                        "email", "vuthif@email.com",
                        "date", "07/01/2026",
                        "items", 1,
                        "total", 95000,
                        "status", 3   // 3: Đã hủy
                )
        ));

        return "views/admin/orders";
    }
}
