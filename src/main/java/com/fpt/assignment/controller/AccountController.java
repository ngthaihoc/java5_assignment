package com.fpt.assignment.controller;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.fpt.assignment.entity.Order;
import com.fpt.assignment.entity.Account;
import com.fpt.assignment.repository.AccountRepository;
import com.fpt.assignment.repository.OrderDetailRepository;
import com.fpt.assignment.repository.OrderRepository;
import com.fpt.assignment.service.AuthService;
import com.fpt.assignment.service.UploadService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth/account")
public class AccountController {

    @Autowired
    OrderDetailRepository detailRepo;

    @Autowired
    HttpSession session;

    @Autowired
    AccountRepository accRepo;

    @Autowired
    OrderRepository orderRepo;

    @Autowired
    AuthService authService;

    @Autowired
    UploadService upService;

    @GetMapping("")
    public String viewAccount(Model model) {
        Account user = authService.getLoggedAccount();
        model.addAttribute("user", user);

        session.setAttribute("user", user);

        model.addAttribute("currentTab", "profile");
        return "views/account";
    }

    @PostMapping("/update")
    public String updateAccount(
            @RequestParam("fullname") String fullname,
            @RequestParam("photo") MultipartFile photo) {

        Account currentUser = authService.getLoggedAccount();
        System.out.println(currentUser.getEmail());
        System.out.println(currentUser.getFullname());
        System.out.println(currentUser.getPassword());
        currentUser.setFullname(fullname);

        if (!photo.isEmpty()) {
            String uploadDir = System.getProperty("user.dir")
                    + "/src/main/resources/static/images/";

            File savedFile = upService.save(photo, uploadDir);

            if (savedFile != null) {
                currentUser.setAvatar(savedFile.getName());
            }
        }

        accRepo.save(currentUser);
        System.out.println("testing avatar " + currentUser.getAvatar());

        return "redirect:/auth/account";
    }

    @GetMapping("/orders")
    public String viewOrders(Model model, HttpSession session) {
        Account user = authService.getLoggedAccount();

        List<Order> list = orderRepo.findByUsername(user.getEmail());

        // Calculate totalValue for each order
        for (Order o : list) {
            java.math.BigDecimal total = o.getOrderDetails().stream()
                    .map(d -> d.getPrice().multiply(java.math.BigDecimal.valueOf(d.getQuantity())))
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);

            if (o.getShippingFee() != null) {
                total = total.add(o.getShippingFee());
            }
            o.setTotalValue(total);
        }

        model.addAttribute("user", user);
        model.addAttribute("orders", list);
        model.addAttribute("currentTab", "orders");
        return "views/account";
    }

    @PostMapping("/change-password")
    public String changePassword(Model model, HttpSession session,
            @RequestParam("oldPass") String oldPass,
            @RequestParam("newPass") String newPass,
            @RequestParam("confirmPass") String confirmPass) {
        Account user = authService.getLoggedAccount();
        model.addAttribute("user", user);
        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            model.addAttribute("error", "Vui lòng điền đầy đủ thông tin");
            model.addAttribute("currentTab", "password");
            return "views/account";
        }

        if (!user.getPassword().equals(oldPass)) {
            model.addAttribute("error", "Mật khẩu cũ không đúng");
            model.addAttribute("currentTab", "password");
            return "views/account";
        }

        if (!newPass.equals(confirmPass)) {
            model.addAttribute("error", "Xác nhận mật khẩu mới không trùng khớp");
            model.addAttribute("currentTab", "password");
            return "views/account"; // Kết thúc sớm nếu sai
        }

        user.setPassword(newPass);
        accRepo.save(user);

        model.addAttribute("message", "Đổi mật khẩu thành công!");
        model.addAttribute("currentTab", "password");
        return "views/account";
    }

    @GetMapping("/change-password")
    public String changePassword(Model model) {
        Account user = authService.getLoggedAccount();
        model.addAttribute("user", user);
        model.addAttribute("currentTab", "password");

        return "views/account";
    }

}