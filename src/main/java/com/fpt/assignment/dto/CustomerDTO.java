package com.fpt.assignment.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CustomerDTO {

    private String email;
    private String name;
    private String phone;

    private long orders;           
    private BigDecimal total;       
    private LocalDateTime lastOrder;

    private String rank;            
    private boolean status;         
}

