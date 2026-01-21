package com.fpt.assignment.repository;

import com.fpt.assignment.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartDetailRepository
        extends JpaRepository<CartDetail, Long> {

    List<CartDetail> findByCart_Id(Long cartId);

    CartDetail findByCart_IdAndBook_Id(Long cartId, Long bookId);
}

