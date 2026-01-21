package com.fpt.assignment.controller;

import com.fpt.assignment.repository.AccountRepository;
import com.fpt.assignment.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @Autowired
    OrderDetailRepository detailRepo;

    @Autowired
    AccountRepository accRepo;

    @GetMapping("/account")
    public String viewAccount(Model model) {
        model.addAttribute("userFullname", "Nguyễn Học");
        model.addAttribute("currentTab", "profile");

        return "views/account";
    }

    @GetMapping("/account/profile")
    public String viewProfile(Model model) {
        model.addAttribute("userFullname", "Nguyễn Học");
        model.addAttribute("currentTab", "profile");
        return "views/account";
    }

    @GetMapping("/account/orders")
    public String viewOrders(Model model) {
        model.addAttribute("userFullname", "Nguyễn Học");
        model.addAttribute("currentTab", "orders");
        return "views/account";
    }

    @GetMapping("/account/change-password")
    public String viewChangePassword(Model model) {
        model.addAttribute("userFullname", "Nguyễn Học");
        model.addAttribute("currentTab", "password");
        return "views/account";
    }

    @GetMapping("/account/address")
    public String viewAddress(Model model) {
        model.addAttribute("userFullname", "Nguyễn Học");
        model.addAttribute("currentTab", "address");
        return "views/account";
    }

    @GetMapping("/account/statistics")
    public String viewStatistics(Model model) {
        model.addAttribute("categoryStats", detailRepo.reportByCategory());

        model.addAttribute("topBook", detailRepo.findTopSellingBooks(PageRequest.of(0, 1)));
        model.addAttribute("lowBook", detailRepo.findLowestSellingBooks(PageRequest.of(0, 1)));

        model.addAttribute("vipCustomers", detailRepo.reportVIPCustomers(PageRequest.of(0, 10)));

        model.addAttribute("currentTab", "statistics");
        return "views/account";
    }

}