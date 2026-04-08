package com.fpt.assignment.service;

import com.fpt.assignment.entity.Book;
import com.fpt.assignment.entity.Order;
import com.fpt.assignment.repository.OrderDetailRepository;
import com.fpt.assignment.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    public Order findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public List<Order> findByEmail(String email) {
        return orderRepository.findByEmail(email);
    }

    public List<Book> findPurchasedBooks(String email) {
        return orderRepository.findPurchasedBooks(email);
    }
}

