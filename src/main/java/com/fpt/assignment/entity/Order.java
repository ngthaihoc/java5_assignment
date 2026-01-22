package com.fpt.assignment.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_date")
    private LocalDateTime createdAt;

    private String address;
    private String phone;
    private int status;

    @ManyToOne
    @JoinColumn(name = "email")
    private Account account;
}

