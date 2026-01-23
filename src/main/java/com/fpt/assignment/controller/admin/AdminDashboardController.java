package com.fpt.assignment.controller.admin;

import com.fpt.assignment.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    OrderDetailRepository detailRepo;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("active", "dashboard");

        model.addAttribute("categoryStats", detailRepo.reportByCategory());

        model.addAttribute("topBook", detailRepo.findTopSellingBooks(PageRequest.of(0, 1)));
        model.addAttribute("lowBook", detailRepo.findLowestSellingBooks(PageRequest.of(0, 1)));

        model.addAttribute("vipCustomers", detailRepo.reportVIPCustomers(PageRequest.of(0, 10)));

        model.addAttribute("currentTab", "statistics");
        return "views/admin/dashboard";
    }
}
