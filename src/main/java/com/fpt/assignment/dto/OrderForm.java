package com.fpt.assignment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderForm {

    @NotBlank(message = "Vui lòng nhập họ tên")
    private String fullName;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Vui lòng nhập email")
    private String email;

    @NotBlank(message = "Vui lòng nhập số điện thoại")
    private String phone;

    @NotBlank(message = "Vui lòng nhập địa chỉ")
    private String address;

    private String city;
    private String district;
    private String ward;
    private String note;

    @NotBlank
    private String paymentMethod; // cod | bank | card

    @NotBlank
    private String shippingMethod; // standard | express
}
