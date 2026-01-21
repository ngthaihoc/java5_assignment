package com.fpt.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpt.assignment.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
    
}
