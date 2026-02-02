package com.fpt.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fpt.assignment.entity.Publisher;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {
}
