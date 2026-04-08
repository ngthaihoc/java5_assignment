package com.fpt.assignment.controller.admin;

import com.fpt.assignment.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/customers")
@RequiredArgsConstructor
public class AdminCustomerRestController {

    final CustomerService customerService;

    @GetMapping
    public ResponseEntity<?> getCustomers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String rank,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(customerService.searchCustomers(keyword, rank, status));
    }

    @GetMapping("/{email}")
    public ResponseEntity<?> getCustomerDetail(@PathVariable String email) {
        return ResponseEntity.ok(customerService.getCustomerDetail(email));
    }

    @PostMapping
    public ResponseEntity<?> createCustomer(@RequestBody Map<String, String> body) {
        try {
            customerService.createCustomer(body.get("email"), body.get("fullname"), body.get("password"));
            return ResponseEntity.ok(Map.of("message", "Tạo thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCustomer(@RequestBody Map<String, String> body) {
        customerService.updateCustomer(body.get("email"), body.get("fullname"), body.get("password"));
        return ResponseEntity.ok(Map.of("message", "Cập nhật thành công"));
    }

    @PostMapping("/{email}/toggle")
    public ResponseEntity<?> toggleCustomer(@PathVariable String email) {
        customerService.toggleStatus(email);
        return ResponseEntity.ok(Map.of("message", "Cập nhật trạng thái thành công"));
    }
}
