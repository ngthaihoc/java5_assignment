package com.fpt.assignment.controller;

import com.fpt.assignment.dto.CartItemDTO;
import com.fpt.assignment.entity.Account;
import com.fpt.assignment.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    // Helper lấy user từ request attribute (được set bởi JwtFilter)
    private Account getCurrentUser(HttpServletRequest request) {
        return (Account) request.getAttribute("currentUser");
    }

    private ResponseEntity<?> unauthorized() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Vui lòng đăng nhập"));
    }

    @GetMapping
    public ResponseEntity<?> getCart(HttpServletRequest request) {
        Account user = getCurrentUser(request);
        if (user == null) return unauthorized();

        List<CartItemDTO> items = cartService.getCartItems(user.getEmail());
        BigDecimal total = cartService.getTotal(user.getEmail());

        return ResponseEntity.ok(Map.of(
                "cartItems", items,
                "cartTotal", total
        ));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @RequestParam("bookId") String bookId,
            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
            HttpServletRequest request) {

        Account user = getCurrentUser(request);
        if (user == null) return unauthorized();

        try {
            if (quantity == null || quantity < 1) quantity = 1;
            cartService.addToCart(user.getEmail(), Long.parseLong(bookId), quantity);
            return ResponseEntity.ok(Map.of("message", "Đã thêm " + quantity + " sản phẩm vào giỏ hàng!"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Lỗi khi thêm: " + e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(
            @RequestParam Long id,
            @RequestParam int qty,
            HttpServletRequest request) {

        Account user = getCurrentUser(request);
        if (user == null) return unauthorized();

        try {
            cartService.updateQuantity(id, qty);
            return ResponseEntity.ok(Map.of("message", "Đã cập nhật số lượng"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<?> remove(@PathVariable Long id, HttpServletRequest request) {
        Account user = getCurrentUser(request);
        if (user == null) return unauthorized();

        try {
            cartService.remove(id);
            return ResponseEntity.ok(Map.of("message", "Đã xóa sản phẩm"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clear(HttpServletRequest request) {
        Account user = getCurrentUser(request);
        if (user == null) return unauthorized();

        try {
            cartService.clear(user.getEmail());
            return ResponseEntity.ok(Map.of("message", "Đã xóa toàn bộ giỏ hàng"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}