package com.fpt.assignment.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
    @Column
    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @Column(name = "fullname", length = 100, nullable = false)
    private String fullname;

    @Column(name = "avatar")
    private String avatar;

    @Column(name = "admin")
    private boolean admin = false;

    private boolean enabled = true;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Order> orders;
}
