package com.fpt.assignment.service;

import com.fpt.assignment.dto.CartItemDTO;
import com.fpt.assignment.dto.OrderForm;
import com.fpt.assignment.entity.Account;
import com.fpt.assignment.entity.Order;
import com.fpt.assignment.entity.OrderDetails;
import com.fpt.assignment.repository.OrderDetailRepository;
import com.fpt.assignment.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class CheckoutService {

    @Autowired
    CartService cartService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    public ResponseEntity<?> getCheckoutInfo(Account user) {
        List<CartItemDTO> items = cartService.getCartItems(user.getEmail());
        if (items.isEmpty())
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Giỏ hàng trống"));

        return ResponseEntity.ok(Map.of(
                "fullName", user.getFullname(),
                "email", user.getEmail(),
                "cartItems", items,
                "cartTotal", cartService.getTotal(user.getEmail())
        ));
    }

    public ResponseEntity<?> placeOrder(Account user, OrderForm form) {
        List<CartItemDTO> items = cartService.getCartItems(user.getEmail());
        if (items.isEmpty())
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Giỏ hàng trống, không thể đặt hàng"));

        try {
            Order order = new Order();
            order.setAccount(user);
            order.setAddress(form.getAddress());
            order.setPhone(form.getPhone());
            order.setStatus(0);
            order.setCreateDate(LocalDateTime.now());
            orderRepository.save(order);

            items.forEach(item -> {
                OrderDetails d = new OrderDetails();
                d.setOrder(order);
                d.setBook(item.getBook());
                d.setPrice(item.getPrice());
                d.setQuantity(item.getQuantity());
                orderDetailRepository.save(d);
            });

            cartService.clear(user.getEmail());

            return ResponseEntity.ok(Map.of(
                    "message", "Đặt hàng thành công!",
                    "orderId", order.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Lỗi khi đặt hàng: " + e.getMessage()));
        }
    }
}