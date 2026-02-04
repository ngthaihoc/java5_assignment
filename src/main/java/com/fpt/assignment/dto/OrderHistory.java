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
  private String phone;

  public OrderHistory(Long id, LocalDateTime createDate, String address, Integer status, BigDecimal totalPrice) {
    this.id = id;
    this.createDate = createDate;
    this.address = address;
    this.status = status;
    this.totalPrice = totalPrice;
  }
}
