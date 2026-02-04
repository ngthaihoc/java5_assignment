package com.fpt.assignment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_details")
public class CartDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    @Column(name = "added_at")
    private LocalDateTime addedAt;

    @JsonIgnore // Tránh vòng lặp: CartDetail -> Cart -> CartDetail
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @Transient
    private java.math.BigDecimal total;
}
