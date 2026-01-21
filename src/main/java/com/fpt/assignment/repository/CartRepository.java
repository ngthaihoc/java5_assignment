package com.fpt.assignment.repository;

import com.fpt.assignment.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByAccount_Email(String email);
}

