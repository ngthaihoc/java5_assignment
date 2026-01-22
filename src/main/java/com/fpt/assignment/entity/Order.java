package com.fpt.assignment.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK tới accounts.email
    @ManyToOne
    @JoinColumn(name = "account_email")
    private Account account;

    private String address;
    private String phone;
    private int status;

    private LocalDateTime createdAt;
}
