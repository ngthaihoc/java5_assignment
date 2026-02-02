package com.fpt.assignment.service;

import com.fpt.assignment.dto.CustomerDTO;
import com.fpt.assignment.dto.CustomerDetailDTO;

import java.util.List;

public interface CustomerService {
    List<CustomerDTO> getCustomers();

    List<CustomerDTO> searchCustomers(String keyword, String rank, String status);

    CustomerDetailDTO getCustomerDetail(String email);

    void toggleStatus(String email);

    void updateCustomer(String email, String fullname, String password);

    void createCustomer(String email, String fullname, String password);
}
