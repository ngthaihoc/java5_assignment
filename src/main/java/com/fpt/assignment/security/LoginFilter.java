package com.fpt.assignment.security;

import java.io.IOException;
import java.util.Base64;

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

  @Autowired
  AccountRepository accountRepository;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;

    HttpSession session = req.getSession(false);

    boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

    if (!isLoggedIn) {
      Cookie[] cookies = req.getCookies();
      if (cookies != null) {
        for (Cookie c : cookies) {
          System.out.println("name: " + c.getName());
          System.out.println("value: " + c.getValue());
          if ("user".equals(c.getName()) && c.getValue() != null && !c.getValue().equals("")) {
            try {
              String decoded = new String(Base64.getDecoder().decode(c.getValue()));
              String[] parts = decoded.split("\\|");
              String email = parts[0];

              Account user = accountRepository.findById(email).orElseThrow(null);

              // tạo session mới
              if (user != null) {
                session = req.getSession(true);
                session.setAttribute("user", user);
              }

              break;

            } catch (Exception e) {
              e.printStackTrace();
            }
            break;
          }
        }
      }
    }

    chain.doFilter(request, response);
  }

}
