package com.fpt.assignment.controller.admin;

import com.fpt.assignment.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminCustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/admin/customers")
    public String customers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String rank,
            @RequestParam(required = false) String status,
            Model model) {

        model.addAttribute("active", "customers");

        model.addAttribute("customers",
                customerService.searchCustomers(keyword, rank, status));

        return "views/admin/customers";
    }

    @GetMapping("/admin/customers/{email}")
    public String customerDetail(
            @PathVariable String email,
            Model model) {

        model.addAttribute("c",
                customerService.getCustomerDetail(email));

        return "views/admin/customer-detail :: modal";
    }

    @PostMapping("/admin/customers/{email}/toggle")
    @ResponseBody
    public void toggleCustomer(@PathVariable String email) {
        customerService.toggleStatus(email);
    }

    @PostMapping("/admin/customers/create")
    public String createCustomer(
            @RequestParam String email,
            @RequestParam String fullname,
            @RequestParam String password,
            RedirectAttributes ra) {

        try {
            customerService.createCustomer(email, fullname, password);
        } catch (RuntimeException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/admin/customers";
    }

    @PostMapping("/admin/customers/update")
    public String updateCustomer(
            @RequestParam String email,
            @RequestParam String fullname,
            @RequestParam(required = false) String password) {

        customerService.updateCustomer(email, fullname, password);
        return "redirect:/admin/customers";
    }

}
