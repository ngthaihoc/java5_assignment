package com.fpt.assignment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "order_details")
@Data
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal price;
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @JsonIgnore // Tránh vòng lặp: OrderDetails -> Order -> OrderDetails
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}
