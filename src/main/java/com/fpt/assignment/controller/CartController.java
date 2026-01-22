package com.fpt.assignment.controller;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

import com.fpt.assignment.entity.Account;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fpt.assignment.dto.CartItemDTO;
import com.fpt.assignment.service.CartService;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    HttpSession session;

    @Autowired
    CartService cartService;

    // HIỂN THỊ GIỎ HÀNG
    @GetMapping
    public String cart(Model model) {

        Account user = (Account) session.getAttribute("user");
        String email = user.getEmail();

        List<CartItemDTO> items = cartService.getCartItems(email);

        model.addAttribute("cartItems", items);
        model.addAttribute("cartTotal", cartService.getTotal(email));

        return "views/cart/cart";
    }



    // THÊM SÁCH
    @GetMapping("/add/{id}")
    public String add(@PathVariable Long id,
                      @CookieValue("user") String userCookie) {

        String email = new String(Base64.getDecoder().decode(userCookie));
        cartService.addBook(email, id);

        return "redirect:/cart";
    }

    // XÓA 1 SẢN PHẨM
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id) {

        cartService.remove(id);
        return "redirect:/cart";
    }

    // CẬP NHẬT SỐ LƯỢNG
    @GetMapping("/update")
    public String update(@RequestParam Long id,
                         @RequestParam int qty) {

        cartService.updateQuantity(id, qty);
        return "redirect:/cart";
    }

    // XÓA TOÀN BỘ GIỎ
    @GetMapping("/clear")
    public String clear(@CookieValue("user") String userCookie) {

        String email = new String(Base64.getDecoder().decode(userCookie));
        cartService.clear(email);

        return "redirect:/cart";
    }
}
