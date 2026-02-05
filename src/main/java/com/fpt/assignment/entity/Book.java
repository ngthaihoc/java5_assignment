package com.fpt.assignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

    @Column(name = "is_ebook")
    private Boolean isEbook = false;

    private Boolean available = true;

    // Chỉ lấy id và name của Category, tránh load books
    @JsonIgnoreProperties({ "books" })
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    // Chỉ serialize các field cần thiết của Publisher
    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;

    @JsonIgnore
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
    private List<CartDetail> cartDetails;

    @Transient
    private BigDecimal oldPrice;
}
