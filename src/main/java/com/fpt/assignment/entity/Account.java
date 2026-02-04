package com.fpt.assignment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Account {
    @Id
    @Column
    private String email;

    @JsonIgnore // Bảo mật: không trả password về client
    @Column(nullable = false)
    private String password;

    @Column(name = "fullname", length = 100, nullable = false)
    private String fullname;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "admin")
    private boolean admin = false;

    private boolean enabled = true;

    @JsonIgnore // Tránh vòng lặp: Account -> Order -> Account
    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Order> orders;

    // ================= TRANSIENT FIELDS (REPLACING DTOs) =================
    @Transient
    private String rank;

    @Transient
    private BigDecimal totalSpent;

    @Transient
    private long orderCount;

    @Transient
    private LocalDateTime latestOrder;

    @Transient
    private String address; // For customer detail
}
