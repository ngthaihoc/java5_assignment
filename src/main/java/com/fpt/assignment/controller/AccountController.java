package com.fpt.assignment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {


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
        model.addAttribute("userFullname", "Nguyễn Học");
        model.addAttribute("currentTab", "statistics");
        return "views/account";
    }

}