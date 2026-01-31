package com.fpt.assignment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistory {
  private Long id;
  private LocalDateTime createDate;
  private String address;
  private Integer status = 0;
  private BigDecimal totalPrice;
}
