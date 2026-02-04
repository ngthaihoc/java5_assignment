package com.fpt.assignment.repository;

import com.fpt.assignment.entity.Book;
import com.fpt.assignment.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.account.email = ?1")
    List<Order> findByEmail(String email);

    @Query("SELECT DISTINCT d.book FROM OrderDetails d WHERE d.order.account.email = ?1")
    List<Book> findPurchasedBooks(String email);

    @Query("SELECT o FROM Order o WHERE o.account.email = ?1 ORDER BY o.createDate DESC")
    List<Order> findByUsername(String email);
}
