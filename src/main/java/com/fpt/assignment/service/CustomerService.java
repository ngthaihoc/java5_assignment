package com.fpt.assignment.service;

import com.fpt.assignment.entity.Account;

import java.util.List;

public interface CustomerService {
    List<Account> getCustomers();

    List<Account> searchCustomers(String keyword, String rank, String status);

    Account getCustomerDetail(String email);

    void toggleStatus(String email);

    void updateCustomer(String email, String fullname, String password);

    void createCustomer(String email, String fullname, String password);
}
