package com.fpt.assignment.service;

import com.fpt.assignment.dto.CartItemDTO;
import com.fpt.assignment.dto.OrderForm;
import com.fpt.assignment.entity.Account;
import com.fpt.assignment.entity.Order;
import com.fpt.assignment.entity.OrderDetails;
import com.fpt.assignment.repository.BookRepository;
import com.fpt.assignment.repository.OrderDetailRepository;
import com.fpt.assignment.repository.OrderRepository;
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
        order.setAccount(user);
        order.setAddress(form.getAddress());
        order.setPhone(form.getPhone());
        order.setStatus(0);
        order.setCreatedAt(LocalDateTime.now());

        orderRepo.save(order);

        for (CartItemDTO item : items) {
            OrderDetails d = new OrderDetails();
            d.setOrder(order);
            d.setBook(item.getBook());
            d.setPrice(item.getPrice());
            d.setQuantity(item.getQuantity());

            detailRepo.save(d);
        }

        // clear cart sau khi đặt hàng
        cartService.clear(user.getEmail());
    }
}

