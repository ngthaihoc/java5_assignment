package com.fpt.assignment.controller;

import com.fpt.assignment.entity.Account;
import com.fpt.assignment.entity.Order;
import com.fpt.assignment.entity.OrderDetails;
import com.fpt.assignment.repository.OrderDetailRepository;
import com.fpt.assignment.repository.OrderRepository;
import com.fpt.assignment.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    CartService cartService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @GetMapping
    public String checkout(Model model, HttpSession session) {

        Account user = (Account) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        Order order = new Order();
        order.setAccount(user);
        // Form needs fullname, phone, address. Account has fullname. Phone/Address
        // might be empty.
        // We can pass them as separate model attributes or prepopulate order.
        model.addAttribute("order", order);
        model.addAttribute("fullName", user.getFullname()); // Pass separately if needed or used in form
        model.addAttribute("email", user.getEmail());

        model.addAttribute(
                "cartItems",
                cartService.getCartItems(user.getEmail()));
        model.addAttribute(
                "cartTotal",
                cartService.getTotal(user.getEmail()));

        return "views/checkout/checkout";
    }

    @PostMapping
    @Transactional
    public String placeOrder(@RequestParam("address") String address,
            @RequestParam("phone") String phone,
            @RequestParam(value = "note", required = false) String note,
            @RequestParam(value = "shippingMethod", defaultValue = "standard") String shippingMethod,
            HttpSession session,
            RedirectAttributes redirect) {

        Account user = (Account) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        // Tính phí vận chuyển
        java.math.BigDecimal shippingFee = java.math.BigDecimal.ZERO;
        if ("express".equals(shippingMethod)) {
            shippingFee = new java.math.BigDecimal("50000");
        }

        Order order = new Order();
        order.setAccount(user);
        order.setAddress(address);
        order.setPhone(phone);
        order.setNote(note);
        order.setStatus(0);
        order.setCreateDate(LocalDateTime.now());
        order.setShippingFee(shippingFee);

        orderRepository.save(order);

        // LƯU CHI TIẾT ĐƠN HÀNG
        cartService.getCartItems(user.getEmail())
                .forEach(item -> {
                    OrderDetails d = new OrderDetails();
                    d.setOrder(order);
                    d.setBook(item.getBook());
                    d.setPrice(item.getBook().getPrice());
                    d.setQuantity(item.getQuantity());
                    orderDetailRepository.save(d);
                });

        // XÓA GIỎ
        cartService.clear(user.getEmail());

        redirect.addFlashAttribute("orderSuccess", true);
        return "redirect:/cart";
    }
}
