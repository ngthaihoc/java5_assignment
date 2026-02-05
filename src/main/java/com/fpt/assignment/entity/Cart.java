package com.fpt.assignment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // FK → cart.email → accounts.email
    @JsonIgnore // Tránh vòng lặp: Cart -> Account
    @ManyToOne
    @JoinColumn(name = "email")
    private Account account;

    // optional: mapping ngược
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartDetail> details;

}
