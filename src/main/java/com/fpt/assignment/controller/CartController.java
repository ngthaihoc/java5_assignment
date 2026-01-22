package com.fpt.assignment.controller;

import java.util.List;

import com.fpt.assignment.dto.CartItemDTO;
import com.fpt.assignment.entity.Account;
import com.fpt.assignment.service.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    HttpSession session;

    // HIỂN THỊ GIỎ HÀNG
    @GetMapping
    public String cart(Model model) {

        Account user = (Account) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        String email = user.getEmail();

        List<CartItemDTO> items = cartService.getCartItems(email);

        model.addAttribute("cartItems", items);
        model.addAttribute("cartTotal", cartService.getTotal(email));

        return "views/cart/cart"; // cart.html
    }

    // THÊM SÁCH
    @GetMapping("/add/{id}")
    public String add(@PathVariable Long id) {

        Account user = (Account) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        cartService.addBook(user.getEmail(), id);
        return "redirect:/cart";
    }

    // XÓA 1 SẢN PHẨM
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id) {

        Account user = (Account) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        cartService.remove(id);
        return "redirect:/cart";
    }

    // CẬP NHẬT SỐ LƯỢNG
    @GetMapping("/update")
    public String update(@RequestParam Long id,
                         @RequestParam int qty) {

        Account user = (Account) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        cartService.updateQuantity(id, qty);
        return "redirect:/cart";
    }

    // XÓA TOÀN BỘ GIỎ
    @GetMapping("/clear")
    public String clear() {

        Account user = (Account) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        cartService.clear(user.getEmail());
        return "redirect:/cart";
    }
}
