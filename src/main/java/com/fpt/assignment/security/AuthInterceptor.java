package com.fpt.assignment.security;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fpt.assignment.entity.Account;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    HttpSession session = request.getSession();
    Account user = (Account) session.getAttribute("user");

    // 1. Kiểm tra đăng nhập
    if (user == null) {
      session.setAttribute("back-url", request.getRequestURI());
      response.sendRedirect("/login");
      return false; // Chặn đứng request, không cho vào Controller
    }

    // 2. Kiểm tra quyền Admin
    String uri = request.getRequestURI();
    if (uri.contains("/admin") && !user.isAdmin()) {
      response.sendRedirect("/login?error=denied");
      return false;
    }

    return true; // Cho phép đi tiếp
  }
}
