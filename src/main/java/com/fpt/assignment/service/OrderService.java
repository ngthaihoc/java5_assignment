package com.fpt.assignment.service;

import com.fpt.assignment.entity.Book;
import com.fpt.assignment.entity.Order;
import java.util.List;


public interface OrderService {
  Order findById(Long id);

  List<Order> findByEmail(String email);

  List<Book> findPurchasedBooks(String email);
}