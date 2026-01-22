package com.fpt.assignment.controller;

import com.fpt.assignment.entity.Account;
import com.fpt.assignment.entity.Order;
import com.fpt.assignment.repository.AccountRepository;
import com.fpt.assignment.repository.OrderDetailRepository;
import com.fpt.assignment.repository.OrderRepository;
import com.fpt.assignment.service.AuthService;
import com.fpt.assignment.service.UploadService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
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


    @GetMapping("/account")
    public String viewAccount(Model model) {
        Account user = authService.getLoggedAccount();
        model.addAttribute("user", user);

        session.setAttribute("user", user);

        model.addAttribute("currentTab", "profile");
        return "views/account";
    }

    @PostMapping("/account/update")
    public String updateAccount(@RequestParam("fullname") String fullname,
                                @RequestParam("photo") MultipartFile photo,
                                HttpSession session) {

        Account currentUser = authService.getLoggedAccount();
        currentUser.setFullname(fullname);

        if (!photo.isEmpty()) {
            try {
                String folderPath = "src/main/resources/static/images/";
                File folder = new File(folderPath);
                if (!folder.exists()) folder.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
                Path path = Paths.get(folderPath + fileName);

                Files.copy(photo.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                currentUser.setAvatar(fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "redirect:/account";
    }


    @GetMapping("/account/orders")
    public String viewOrders(Model model, HttpSession session) {
        Account user = authService.getLoggedAccount();

        List<Order> list = orderRepo.findByUsername(user.getEmail());
        model.addAttribute("orders", list);
        model.addAttribute("currentTab", "orders");
        return "views/account";
    }

    @PostMapping("/account/change-password")
    public String changePassword(Model model, HttpSession session,
                                 @RequestParam("oldPass") String oldPass,
                                 @RequestParam("newPass") String newPass,
                                 @RequestParam("confirmPass") String confirmPass) {
        Account user = authService.getLoggedAccount();


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

    @GetMapping("/account/change-password")
    public String changePassword(Model model) {
        model.addAttribute("currentTab", "password");
        return "views/account";
    }

//    @GetMapping("/account/address")
//    public String viewAddress(Model model) {
//        model.addAttribute("userFullname", "Nguyễn Học");
//        model.addAttribute("currentTab", "address");
//        return "views/account";
//    }

    @GetMapping("/account/statistics")
    public String viewStatistics(Model model) {
        model.addAttribute("categoryStats", detailRepo.reportByCategory());

        model.addAttribute("topBook", detailRepo.findTopSellingBooks(PageRequest.of(0, 1)));
        model.addAttribute("lowBook", detailRepo.findLowestSellingBooks(PageRequest.of(0, 1)));

        model.addAttribute("vipCustomers", detailRepo.reportVIPCustomers(PageRequest.of(0, 10)));

        model.addAttribute("currentTab", "statistics");
        return "views/account";
    }


}