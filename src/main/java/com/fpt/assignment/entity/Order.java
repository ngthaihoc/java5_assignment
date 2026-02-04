package com.fpt.assignment.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    private String address;
    private String phone;
    private int status;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column(name = "shipping_fee")
    private BigDecimal shippingFee = BigDecimal.ZERO;

    @JsonIgnore // Tránh vòng lặp: Order -> Account -> Order
    @ManyToOne
    @JoinColumn(name = "email")
    private Account account;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetails> orderDetails;

    @Transient
    private BigDecimal totalValue;
}
