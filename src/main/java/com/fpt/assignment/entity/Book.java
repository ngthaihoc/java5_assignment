package com.fpt.assignment.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String image;

    @Column(name = "author_name")
    private String authorName;

    @Column(nullable = false)
    private BigDecimal price;

    private Integer quantity;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    private String dimensions;

    private String translator;

    @Column(name = "cover_type")
    private String coverType;

    @Column(name = "page_count")
    private Integer pageCount;

    private Boolean available = true;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;
}
