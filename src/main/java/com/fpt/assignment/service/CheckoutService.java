package com.fpt.assignment.service;

import com.fpt.assignment.dto.CartItemDTO;
import com.fpt.assignment.dto.OrderForm;
import com.fpt.assignment.entity.*;
import com.fpt.assignment.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class CheckoutService {

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    OrderDetailRepository detailRepo;

    @Autowired
    BookRepository bookRepo;

    @Autowired
    CartService cartService;

    public void placeOrder(Account user, OrderForm form, List<CartItemDTO> items) {

        Order order = new Order();
        order.setAccount(user); // user là Account (email PK)
        order.setAddress(form.getAddress());
        order.setPhone(form.getPhone());
        order.setStatus(0);
        order.setCreatedAt(LocalDateTime.now());

        orderRepo.save(order);

        for (CartItemDTO item : items) {
            OrderDetail d = new OrderDetail();
            d.setOrder(order);

            Book book = bookRepo.findById(item.getBookId())
                    .orElseThrow();
            d.setBook(book);

            d.setPrice(item.getPrice());
            d.setQuantity(item.getQuantity());

            detailRepo.save(d);
        }

        // clear cart sau khi đặt hàng
        cartService.clear(user.getEmail());
    }
}

