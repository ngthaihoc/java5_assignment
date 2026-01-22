package com.fpt.assignment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDetailDTO {
    private Long id;
    private String title;
    private String image;
    private String authorName;
    private BigDecimal price;
    private BigDecimal oldPrice; // nếu có giảm giá
    private Integer quantity;
    private String description;
    private LocalDate publishDate;
    private String dimensions;
    private String translator;
    private String coverType;
    private Integer pageCount;
    private Boolean available;
    private String categoryName;
    private String publisherName;
}
