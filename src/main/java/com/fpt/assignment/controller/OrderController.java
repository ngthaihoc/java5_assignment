package com.fpt.assignment.controller;

import com.fpt.assignment.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    HttpServletRequest request;

    @RequestMapping("/list")
    public String list(Model model) {
        String email = request.getRemoteUser();
        model.addAttribute("orders", orderService.findByEmail(email));
        return "views/order/order-list";
    }

    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        System.out.println(id);
        model.addAttribute("order", orderService.findById(id));
        return "views/order/order-detail";
    }

    @RequestMapping("/my-product-list")
    public String myProductList(Model model) {
        String email = request.getRemoteUser();
        model.addAttribute("books", orderService.findPurchasedBooks(email));
        return "views/order/my-product-list";
    }
}
