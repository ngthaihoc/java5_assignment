package com.fpt.assignment.repository;

import com.fpt.assignment.dto.OrderHistory;
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

    @Query("SELECT new com.fpt.assignment.dto.OrderHistory(" +
            "o.id, o.createDate, o.address, o.status, SUM(d.price * d.quantity)) " +
            "FROM Order o " +
            "JOIN o.orderDetails d " +
            "WHERE o.account.email = ?1 " +
            "GROUP BY o.id, o.createDate, o.address, o.status")
    List<OrderHistory> findByUsername(String email);
}
