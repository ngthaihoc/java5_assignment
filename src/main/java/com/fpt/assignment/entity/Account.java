package com.fpt.assignment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
  @Id
  @Column(length = 100)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(name = "fullname", length = 100, nullable = false)
  private String fullname;

  private String avatar;
  private boolean admin = false;
}
