package com.fpt.assignment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fpt.assignment.entity.CartDetail;
import com.fpt.assignment.entity.Account;
import com.fpt.assignment.service.CartService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    CartService cartService;

    @Autowired
    HttpSession session;

    @RequestMapping
    public String cart(Model model) {

        Account user = (Account) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        String email = user.getEmail();

        List<CartDetail> items = cartService.getCartItems(email);

        model.addAttribute("cartItems", items);
        model.addAttribute("cartTotal", cartService.getTotal(email));

        return "views/cart/cart"; // cart.html
    }

    // // THÊM SÁCH
    // @GetMapping("/add/{id}")
    // public String add(@PathVariable Long id) {

    // Account user = (Account) session.getAttribute("user");
    // if (user == null) {

    // return "redirect:/login";
    // }
    // if (id == null) return "views/cart/cart";

    // cartService.addBook(user.getEmail(), id);
    // return "redirect:/cart";
    // }

    // XÓA 1 SẢN PHẨM
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id) {

        Account user = (Account) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        cartService.remove(id);
        return "redirect:/cart";
    }

    // CẬP NHẬT SỐ LƯỢNG
    @GetMapping("/update")
    public String update(@RequestParam Long id,
            @RequestParam int qty) {

        Account user = (Account) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        cartService.updateQuantity(id, qty);
        return "redirect:/cart";
    }

    // XÓA TOÀN BỘ GIỎ
    @GetMapping("/clear")
    public String clear() {

        Account user = (Account) session.getAttribute("user");
        if (user == null)
            return "redirect:/login";

        cartService.clear(user.getEmail());
        return "redirect:/cart";
    }

    // Thêm vào giỏ ở trang chi tiết
    @PostMapping("/add")
    public String addToCart(@RequestParam("bookId") String bookId,
            @RequestParam(value = "quantity", defaultValue = "1") Integer quantity,
            @RequestParam(value = "action", defaultValue = "addToCart") String action,
            @RequestParam(value = "returnUrl", required = false) String returnUrl,
            RedirectAttributes redirectAttributes) {

        Account user = (Account) session.getAttribute("user");

        // Kiểm tra đăng nhập
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng!");
            return "redirect:/login";
        }

        String email = user.getEmail();

        try {
            if (quantity == null || quantity < 1) {
                quantity = 1;
            }

            cartService.addToCart(email, Long.parseLong(bookId), quantity);

            redirectAttributes.addFlashAttribute("success", "Đã thêm " + quantity + " sản phẩm vào giỏ hàng!");

            if ("buyNow".equals(action)) {
                return "redirect:/cart";
            } else if (returnUrl != null && !returnUrl.isEmpty()) {
                return "redirect:" + returnUrl;
            } else {
                return "redirect:/book/" + bookId;
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi khi thêm: " + e.getMessage());
            if (returnUrl != null && !returnUrl.isEmpty()) {
                return "redirect:" + returnUrl;
            }
            return "redirect:/book/" + bookId;
        }
    }
}
