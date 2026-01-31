package com.fpt.assignment.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    private static List<Map<String, Object>> orders = new ArrayList<>(List.of(
            Map.of("id", 1234, "customer", "Nguyễn Văn A", "email", "nguyenvana@email.com", "date", "10/01/2026", "items", 3, "total", 247000.0, "status", 1),
            Map.of("id", 1235, "customer", "Trần Thị B", "email", "tranthib@email.com", "date", "09/01/2026", "items", 2, "total", 168000.0, "status", 2),
            Map.of("id", 1236, "customer", "Lê Văn C", "email", "levanc@email.com", "date", "12/01/2026", "items", 1, "total", 79000.0, "status", 0),
            Map.of("id", 1239, "customer", "Vũ Thị F", "email", "vuthif@email.com", "date", "07/01/2026", "items", 1, "total", 95000.0, "status", 3)
    ));

    @GetMapping
    public String orders(Model model, 
                         @RequestParam(required = false) String search, 
                         @RequestParam(required = false) Integer status) {
        
        List<Map<String, Object>> filteredOrders = orders;

        if (search != null && !search.isEmpty()) {
            filteredOrders = filteredOrders.stream()
                .filter(o -> o.get("customer").toString().toLowerCase().contains(search.toLowerCase()) || 
                             o.get("id").toString().contains(search))
                .collect(Collectors.toList());
        }

        if (status != null) {
            filteredOrders = filteredOrders.stream()
                .filter(o -> o.get("status").equals(status))
                .collect(Collectors.toList());
        }

        model.addAttribute("active", "orders");
        model.addAttribute("orders", filteredOrders);
        return "views/admin/orders";
    }

    @PostMapping("/create")
    public String createOrder(@RequestParam String customer, @RequestParam String email, @RequestParam Double total) {
        int newId = (int) (Math.random() * 9000) + 1000;
        orders.add(new java.util.HashMap<>(Map.of(
            "id", newId, "customer", customer, "email", email, "date", "31/01/2026", "items", 1, "total", total, "status", 0
        )));
        return "redirect:/admin/orders";
    }

    @PostMapping("/update")
    public String updateOrder(@RequestParam Integer id, @RequestParam Integer status) {
        for (Map<String, Object> o : orders) {
            if (o.get("id").equals(id)) {
                java.util.Map<String, Object> mutableOrder = new java.util.HashMap<>(o);
                mutableOrder.put("status", status);
                orders.set(orders.indexOf(o), mutableOrder);
                break;
            }
        }
        return "redirect:/admin/orders";
    }

    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        orders.removeIf(o -> o.get("id").equals(id));
        return "redirect:/admin/orders";
    }
}