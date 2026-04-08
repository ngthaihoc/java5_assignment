package com.fpt.assignment.controller;

import com.fpt.assignment.entity.Account;
import com.fpt.assignment.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    HttpServletRequest request;

    // GET /orders — Danh sách đơn hàng của user
    @GetMapping
    public ResponseEntity<?> getMyOrders() {
        Account user = (Account) request.getAttribute("currentUser");
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Vui lòng đăng nhập"));

        return ResponseEntity.ok(orderService.findByEmail(user.getEmail()));
    }

    // GET /orders/{id} — Chi tiết 1 đơn hàng
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(@PathVariable Long id) {
        Account user = (Account) request.getAttribute("currentUser");
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Vui lòng đăng nhập"));

        var order = orderService.findById(id);
        if (order == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Không tìm thấy đơn hàng"));

        return ResponseEntity.ok(order);
    }

    // GET /orders/purchased-books — Sách đã mua
    @GetMapping("/purchased-books")
    public ResponseEntity<?> getPurchasedBooks() {
        Account user = (Account) request.getAttribute("currentUser");
        if (user == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Vui lòng đăng nhập"));

        return ResponseEntity.ok(orderService.findPurchasedBooks(user.getEmail()));
    }
}