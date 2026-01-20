package com.fpt.assignment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {

    @GetMapping("/cart")
    public String cart() {
        return "views/cart/cart";
    }

    @GetMapping("/orders")
    public String orders() {
        return "views/cart/orders";
    }

    @GetMapping("/orders/detail")
    public String orderDetail() {
        return "views/cart/order-detail";
    }

    @GetMapping("/my-products")
    public String myProducts() {
        return "views/cart/my-products";
    }
}

