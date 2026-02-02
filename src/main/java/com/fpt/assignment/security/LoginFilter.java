package com.fpt.assignment.security;

import java.io.IOException;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

import com.fpt.assignment.entity.Account;
import com.fpt.assignment.repository.AccountRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@WebFilter("/*")
public class LoginFilter extends GenericFilterBean {

  private static final Logger log = LoggerFactory.getLogger(LoginFilter.class);

  @Autowired
  AccountRepository accountRepository;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;
    HttpSession session = req.getSession(false);

    // Nếu chưa đăng nhập, thử tìm cookie để tự động đăng nhập
    if (session == null || session.getAttribute("user") == null) {
      tryAutoLoginFromCookie(req);
    }

    chain.doFilter(request, response);
  }

  private void tryAutoLoginFromCookie(HttpServletRequest req) {
    Cookie[] cookies = req.getCookies();
    if (cookies == null) {
      return;
    }

    for (Cookie cookie : cookies) {
      if ("user".equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().isEmpty()) {
        try {
          String decoded = new String(Base64.getDecoder().decode(cookie.getValue()));
          String[] parts = decoded.split("\\|");

          if (parts.length > 0) {
            String email = parts[0];
            Account user = accountRepository.findById(email).orElse(null);

            if (user != null) {
              HttpSession session = req.getSession(true);
              session.setAttribute("user", user);
              log.info("Auto login successful for user: {}", email);
            } else {
              log.warn("Auto login failed: User not found for email: {}", email);
            }
          }
        } catch (Exception e) {
          log.error("Error during auto login from cookie: {}", e.getMessage());
        }
        break;
      }
    }
  }

}
