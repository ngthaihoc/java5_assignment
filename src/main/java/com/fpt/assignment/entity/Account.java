package com.fpt.assignment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "accounts")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Account {
    @Id
    @Column(length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "fullname", length = 100, nullable = false)
    private String fullname;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "admin")
    private boolean admin = false;
}
