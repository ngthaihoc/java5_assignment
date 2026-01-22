package com.fpt.assignment.dto;

import com.fpt.assignment.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

    private Long cartDetailId;
    //    private Long bookId;
    private Book book;
    private String title;
    private String author;
    private String category;
    private String image;

    private BigDecimal price;
    private int quantity;
    private BigDecimal total;

}

