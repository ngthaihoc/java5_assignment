package com.fpt.assignment.controller;

import com.fpt.assignment.entity.Account;
import com.fpt.assignment.entity.Order;
import com.fpt.assignment.entity.OrderDetail;
import com.fpt.assignment.repository.OrderDetailRepository;
import com.fpt.assignment.repository.OrderRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    OrderDetailRepository detailRepo;

    @Autowired
    HttpSession session;

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {

        Account user = (Account) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn"));

        // 🔒 CHỈ XEM ĐƠN CỦA CHÍNH MÌNH
        if (!order.getAccount().getEmail().equals(user.getEmail())) {
            return "redirect:/";
        }

        List<OrderDetail> details = detailRepo.findByOrder_Id(id);

        model.addAttribute("order", order);
        model.addAttribute("details", details);

        return "views/order/detail";
    }
}

