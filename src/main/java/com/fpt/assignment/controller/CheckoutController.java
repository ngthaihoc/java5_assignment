package com.fpt.assignment.controller;

import java.math.BigDecimal;
import java.util.List;

import com.fpt.assignment.dto.OrderForm;
import com.fpt.assignment.entity.Order;
import com.fpt.assignment.service.CheckoutService;
import jakarta.servlet.http.HttpSession;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.fpt.assignment.dto.CartItemDTO;
import com.fpt.assignment.entity.Account;
import com.fpt.assignment.service.CartService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    CartService cartService;

    @Autowired
    CheckoutService checkoutService;

    @Autowired
    HttpSession session;

    @GetMapping
    public String checkout(Model model) {
        Account user = (Account) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        List<CartItemDTO> items = cartService.getCartItems(user.getEmail());
        if (items.isEmpty()) return "redirect:/cart";

        model.addAttribute("cartItems", items);
        model.addAttribute("cartTotal", cartService.getTotal(user.getEmail()));
        model.addAttribute("orderForm", new OrderForm());

        return "views/checkout/checkout";
    }

    @PostMapping
    public String placeOrder(
            @Valid @ModelAttribute("orderForm") OrderForm form,
            BindingResult result,
            RedirectAttributes redirect) {

        Account user = (Account) session.getAttribute("user");
        List<CartItemDTO> items = cartService.getCartItems(user.getEmail());

        if (result.hasErrors()) {
            return "views/checkout/checkout";
        }

        checkoutService.placeOrder(user, form, items);

        // flash message
        redirect.addFlashAttribute("orderSuccess", true);

        return "redirect:/cart";
    }
}



