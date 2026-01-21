package com.fpt.assignment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @Column(length = 4)
    private String id;

    @Column(nullable = false, length = 100)
    private String name;
}
