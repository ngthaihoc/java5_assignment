package com.fpt.assignment.repository;

import com.fpt.assignment.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByAccount_Email(String email);
}

