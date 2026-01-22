package com.fpt.assignment.controller;

import com.fpt.assignment.dto.OrderForm;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        if (user == null) return "redirect:/login";

        OrderForm form = new OrderForm();
        form.setEmail(user.getEmail());
        form.setFullName(user.getFullname());

        model.addAttribute("orderForm", form);
        model.addAttribute(
                "cartItems",
                cartService.getCartItems(user.getEmail())
        );
        model.addAttribute(
                "cartTotal",
                cartService.getTotal(user.getEmail())
        );

        return "views/checkout/checkout";
    }

    @PostMapping
    public String placeOrder(@ModelAttribute OrderForm form,
                             HttpSession session,
                             RedirectAttributes redirect) {

        Account user = (Account) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Order order = new Order();
        order.setAccount(user);
        order.setAddress(form.getAddress());
        order.setPhone(form.getPhone());
        order.setStatus(0);
        order.setCreateDate(LocalDateTime.now());

        orderRepository.save(order);

        // LƯU CHI TIẾT ĐƠN HÀNG
        cartService.getCartItems(user.getEmail())
                .forEach(item -> {
                    OrderDetails d = new OrderDetails();
                    d.setOrder(order);
                    d.setBook(item.getBook());
                    d.setPrice(item.getPrice());
                    d.setQuantity(item.getQuantity());
                    orderDetailRepository.save(d);
                });

        // XÓA GIỎ
        cartService.clear(user.getEmail());

        redirect.addFlashAttribute("orderSuccess", true);
        return "redirect:/cart";
    }
}
