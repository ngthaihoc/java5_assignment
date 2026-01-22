package com.fpt.assignment.service;

import com.fpt.assignment.entity.Book;
import com.fpt.assignment.entity.Order;
import com.fpt.assignment.repository.OrderRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired OrderRepository dao;

    @Override
    public Order findById(Long id) { return dao.findById(id).get(); }

    @Override
    public List<Order> findByEmail(String email) { return dao.findByEmail(email); }

    @Override
    public List<Book> findPurchasedBooks(String email) { return dao.findPurchasedBooks(email); }
}
