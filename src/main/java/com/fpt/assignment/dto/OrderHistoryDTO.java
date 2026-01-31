package com.fpt.assignment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderHistoryDTO {
    private Long id;
    private LocalDateTime date;
    private int itemCount;
    private BigDecimal total;
    private String status;
}

