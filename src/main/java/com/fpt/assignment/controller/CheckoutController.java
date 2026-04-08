package com.fpt.assignment.controller;

import com.fpt.assignment.dto.OrderForm;
import com.fpt.assignment.entity.Account;

import com.fpt.assignment.service.CheckoutService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    CheckoutService checkoutService;

    @Autowired
    HttpServletRequest request;

    // GET /checkout/info — Lấy thông tin trước khi đặt hàng
    @GetMapping("/info")
    public ResponseEntity<?> getCheckoutInfo() {
        Account user = (Account) request.getAttribute("currentUser");
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Vui lòng đăng nhập"));

        return checkoutService.getCheckoutInfo(user);
    }

    // POST /checkout — Đặt hàng
    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody OrderForm form) {
        Account user = (Account) request.getAttribute("currentUser");
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Vui lòng đăng nhập"));

        return checkoutService.placeOrder(user, form);
    }
}