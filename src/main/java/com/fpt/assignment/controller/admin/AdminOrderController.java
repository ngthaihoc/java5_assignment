package com.fpt.assignment.controller.admin;

import com.fpt.assignment.entity.Order;
import com.fpt.assignment.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired 
    private OrderRepository orderRepo;

    @GetMapping
    public String orders(Model model, 
                         @RequestParam(required = false) String search, 
                         @RequestParam(required = false) Integer status) {
        
        List<Order> list = orderRepo.findAll();

        if (search != null && !search.isEmpty()) {
            list = list.stream()
                .filter(o -> o.getId().toString().contains(search) || 
                             (o.getAccount() != null && o.getAccount().getFullname().toLowerCase().contains(search.toLowerCase())))
                .collect(Collectors.toList());
        }

        if (status != null) {
            list = list.stream().filter(o -> o.getStatus() == status).collect(Collectors.toList());
        }

        model.addAttribute("active", "orders");
        model.addAttribute("orders", list);
        
        model.addAttribute("priceHelper", (java.util.function.Function<Order, Double>) o -> {
            if (o.getOrderDetails() == null) return 0.0;
            return o.getOrderDetails().stream()
                    .mapToDouble(d -> Double.parseDouble(d.getPrice().toString()) * d.getQuantity()).sum();
        });

        return "views/admin/orders";
    }

    @PostMapping("/update")
    public String updateOrder(@RequestParam Long id, 
                             @RequestParam int status,
                             @RequestParam String phone,
                             @RequestParam String address) {
        Order order = orderRepo.findById(id).orElse(null);
        if (order != null) {
            order.setStatus(status);
            order.setPhone(phone);
            order.setAddress(address);
            orderRepo.save(order);
        }
        return "redirect:/admin/orders";
    }

    // Xóa đơn hàng
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id) {
        orderRepo.deleteById(id);
        return "redirect:/admin/orders";
    }
}