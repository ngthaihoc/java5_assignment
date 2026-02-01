package com.fpt.assignment.controller.admin;

import com.fpt.assignment.repository.AccountRepository;
import com.fpt.assignment.repository.BookRepository;
import com.fpt.assignment.repository.OrderDetailRepository;
import com.fpt.assignment.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminDashboardController {
    final OrderRepository orderRepository;
    final AccountRepository accountRepository;
    final BookRepository bookRepository;
    @Autowired
    OrderDetailRepository detailRepo;

    @GetMapping("/dashboard")
    public String dashboard(Model model,
            @RequestParam(value = "tab", required = false, defaultValue = "category") String tab,
            @RequestParam(value = "keyword", required = false) String keyword) {

        model.addAttribute("active", "dashboard");

        long totalOrders = orderRepository.count();
        model.addAttribute("totalOrder", totalOrders);

        long toalUser = accountRepository.countByAdminFalse();
        model.addAttribute("totalCustomers", toalUser);

        model.addAttribute("totalBook", bookRepository.count());

        Long totalBooks = detailRepo.sumTotalBooks();
        long totalBookCount = totalBooks == null ? 0 : totalBooks;

        model.addAttribute("totalBooks", totalBookCount);

        model.addAttribute("tab", tab);

        if ("product".equals(tab)) {
            model.addAttribute("topBook", detailRepo.findTopSellingBooks(PageRequest.of(0, 5)));
            model.addAttribute("lowBook", detailRepo.findLowestSellingBooks(PageRequest.of(0, 5)));

            if (keyword != null && !keyword.isBlank()) {
                model.addAttribute("searchedBooks", detailRepo.findSaleStatsByKeyword(keyword));
                model.addAttribute("keyword", keyword);
            }
        } else if ("customer".equals(tab)) {
            model.addAttribute("vipCustomers", detailRepo.reportVIPCustomers(PageRequest.of(0, 10)));
        } else {
            model.addAttribute("categoryStats", detailRepo.reportByCategory());
        }

        return "views/admin/dashboard";
    }
}