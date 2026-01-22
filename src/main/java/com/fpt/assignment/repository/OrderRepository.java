package com.fpt.assignment.repository;

import com.fpt.assignment.entity.Account;
import com.fpt.assignment.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.account.email = ?1")
    List<Order> findByUsername(String email);

    List<Order> findByAccount(Account account);
}
