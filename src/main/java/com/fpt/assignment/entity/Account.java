package com.fpt.assignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(nullable = false)
    private String password;

    @Column(name = "fullname", length = 100, nullable = false)
    private String fullname;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "admin")
    private boolean admin = false;

    private boolean enabled = true;


    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private List<Order> orders;
}
