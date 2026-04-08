package com.fpt.assignment.controller.admin;

import com.fpt.assignment.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardRestController {

    final OrderRepository orderRepository;
    final AccountRepository accountRepository;
    final BookRepository bookRepository;
    final OrderDetailRepository orderDetailRepository;

    @GetMapping
    public ResponseEntity<?> dashboard(
            @RequestParam(defaultValue = "category") String tab,
            @RequestParam(required = false) String keyword) {

        Map<String, Object> res = new HashMap<>();
        res.put("totalOrder", orderRepository.count());
        res.put("totalCustomers", accountRepository.countByAdminFalse());
        res.put("totalBook", bookRepository.count());
        Long sold = orderDetailRepository.sumTotalBooks();
        res.put("totalBooks", sold == null ? 0 : sold);
        res.put("tab", tab);

        switch (tab) {
            case "product" -> {
                res.put("topBook", orderDetailRepository.findTopSellingBooks(PageRequest.of(0, 5)));
                res.put("lowBook", orderDetailRepository.findLowestSellingBooks(PageRequest.of(0, 5)));
                if (keyword != null && !keyword.isBlank())
                    res.put("searchedBooks", orderDetailRepository.findSaleStatsByKeyword(keyword));
            }
            case "customer" ->
                res.put("vipCustomers", orderDetailRepository.reportVIPCustomers(PageRequest.of(0, 10)));
            default ->
                res.put("categoryStats", orderDetailRepository.reportByCategory());
        }

        return ResponseEntity.ok(res);
    }
}
