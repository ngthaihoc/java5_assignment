package com.fpt.assignment.controller;

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

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

  @Autowired
  XMailer xMailer;

  @Autowired
  AuthService authService;

  @Autowired
  HttpSession session;

  @Autowired
  HttpServletRequest req;

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
        }

        session.setAttribute("user", user);

        if (session.getAttribute("back-url") != null) {
          String backUrl = (String) session.getAttribute("back-url");
          session.removeAttribute("back-url");
          return "redirect:" + backUrl;
        }

        return "redirect:/home";
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
  public String createAccount(@ModelAttribute("user") RegisterForm dto, Model model) {

    boolean validate = authService.isEmailNotExisted(dto.getEmail());

    if (!validate) {
      model.addAttribute("error", "Email đã tồn tại");
      dto.setEmail("");
      model.addAttribute("registerForm", dto);
      return "views/auth/register";
    }

    String otp = authService.generateOTP();

    session.setAttribute("otp", otp);
    session.setAttribute("registerDTO", dto);
    session.setAttribute("otpAction", "register");

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

    if (session.getAttribute("otpAction") != null && session.getAttribute("otpAction") == "forgot") {
      return "views/auth/resetPassword";
    }

    RegisterForm registerDTO = (RegisterForm) session.getAttribute("registerDTO");

    if (userOtp.equals(serverOtp)) {

      Account account = authService.createAccount(registerDTO).orElseThrow(null);

      // Xoá otp và registerDTO trong session
      session.removeAttribute("otp");
      session.removeAttribute("registerDTO");

      // Kiểm tra user có null không trước khi set vào session
      session.setAttribute("user", account);
      return "redirect:/home";

    } else {
      String email = (registerDTO != null) ? registerDTO.getEmail() : "";
      return "redirect:/verify?email=" + email + "&error=true";
    }
  }

  @GetMapping("/forgot-password")
  public String showForgotForm() {
    return "views/auth/forgot";
  }

  @PostMapping("/change-password")
  public String changePassword(@RequestParam("newPassword") String newPassword) {
    String email = (String) session.getAttribute("forgotEmail");
    authService.updatePassword(email, newPassword);

    // Xoá các thuộc tính liên quan trong session
    session.removeAttribute("otp");
    session.removeAttribute("forgotEmail");
    session.removeAttribute("otpAction");

    return "redirect:/login";
  }

  @PostMapping("/forgot-password")
  public String forgotPassword(@RequestParam("email") String email, Model model) {

    boolean validate = authService.isEmailNotExisted(email);

    if (validate) {
      model.addAttribute("error", "Email đã tồn tại");
      return "views/auth/forgot";
    }

    String otp = authService.generateOTP();

    session.setAttribute("otp", otp);
    session.setAttribute("forgotEmail", email);
    session.setAttribute("otpAction", "forgot");

    // Gửi email
    String subject = "Đổi mật khẩu tài khoản";
    String body = "Mã OTP của bạn là: " + otp;
    XMailer.send(email, subject, body);

    return "redirect:/verify?email=" + email;
  }

  @GetMapping("/test")
  public String getMethodName() {
    System.out.println("Test auth controller" + session.getAttribute("user"));
    return "views/auth/resetPassword";
  }

  @GetMapping("/logout")
  public String logout() {
    // 1. Huỷ Session
    if (session != null) {
      session.invalidate();
    }

    // 2. Xoá sạch Cookie
    Cookie cookie = new Cookie("user", "");
    cookie.setMaxAge(0); // Xóa cookie
    cookie.setPath("/");
    response.addCookie(cookie);

    return "redirect:/login";

  }

}
