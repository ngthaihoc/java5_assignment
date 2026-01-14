package com.fpt.assignment.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fpt.assignment.dto.LoginForm;
import com.fpt.assignment.dto.RegisterForm;
import com.fpt.assignment.entity.Account;
import com.fpt.assignment.service.AuthService;
import com.fpt.assignment.utils.XMailer;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {

  @Autowired
  XMailer xMailer;

  @Autowired
  AuthService authService;

  @Autowired
  HttpSession session;

  @Autowired
  HttpServletResponse response;

  @GetMapping("/login")
  public String showLoginForm(Model model) {
    model.addAttribute("loginForm", new LoginForm());
    return "views/auth/login";
  }

  @PostMapping("/login")
  public String login(@ModelAttribute LoginForm dto, Model model) {
    try {
      Account user = authService.authenticate(dto);
      if (user != null) {

        if (dto.isRememberMe()) {
          authService.saveAccountToCookie(user, response);
        } else {
          session.setAttribute("user", user);
        }

        return "redirect:/";
      }
      model.addAttribute("error", "Sai email hoặc mật khẩu");
    } catch (RuntimeException e) {
      model.addAttribute("error", e.getMessage());
    }
    return "views/auth/login";
  }

  @GetMapping("/register")
  public String showRegisterForm(Model model) {
    model.addAttribute("registerForm", new RegisterForm());
    return "views/auth/register";
  }

  @PostMapping("/register")
  public String createAccount(@ModelAttribute("user") RegisterForm dto) {
    String otp = authService.generateOTP();

    session.setAttribute("otp", otp);
    session.setAttribute("registerDTO", dto);

    // Gửi email
    String subject = "Xác thực tài khoản";
    String body = "Mã OTP của bạn là: " + otp;
    XMailer.send(dto.getEmail(), subject, body);

    return "redirect:/verify?email=" + dto.getEmail();
  }

  @GetMapping("/verify")
  public String showVerifyForm(@RequestParam("email") String email, Model model,
      @RequestParam(value = "error", required = false) String error) {
    model.addAttribute("email", email);
    model.addAttribute("error", error != null);
    return "views/auth/verify";
  }

  @PostMapping("/verify-otp")
  public String verify(HttpSession session, HttpServletResponse response, @RequestParam("otpCode") String userOtp) {
    String serverOtp = (String) session.getAttribute("otp");
    RegisterForm registerDTO = (RegisterForm) session.getAttribute("registerDTO");

    if (userOtp.equals(serverOtp)) {

      Optional<Account> account = authService.createAccount(registerDTO);

      // Thành công
      session.setAttribute("user", account);
      session.removeAttribute("otp");
      session.removeAttribute("registerDTO");
      return "redirect:/";
    } else {
      String email = (registerDTO != null) ? registerDTO.getEmail() : "";
      return "redirect:/verify?email=" + email + "&error=true";
    }
  }

}
