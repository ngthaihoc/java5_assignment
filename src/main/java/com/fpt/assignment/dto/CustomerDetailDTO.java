package com.fpt.assignment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class CustomerDetailDTO {

    private String email;
    private String name;
    private String address;
    private String avatar;

    private long totalOrders;
    private BigDecimal totalAmount;
    private LocalDateTime lastOrder;
    private LocalDateTime registerDate;

    private String rank;
    private boolean status;

    private List<OrderHistoryDTO> orders;
}

