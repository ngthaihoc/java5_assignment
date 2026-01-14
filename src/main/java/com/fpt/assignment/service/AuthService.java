package com.fpt.assignment.service;

import java.net.HttpCookie;
import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpt.assignment.dto.LoginForm;
import com.fpt.assignment.dto.RegisterForm;
import com.fpt.assignment.entity.Account;
import com.fpt.assignment.repository.AccountRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
public class AuthService {

  @Autowired
  AccountRepository accountRepository;

  public String generateOTP() {
    SecureRandom random = new SecureRandom();
    int otp = 100000 + random.nextInt(900000); // Tạo số từ 100000 đến 999999
    return String.valueOf(otp);
  }

  public Optional<Account> createAccount(RegisterForm dto) {
    Account newAccount = new Account();

    newAccount.setEmail(dto.getEmail());
    newAccount.setFullname(dto.getFirstName() + " " + dto.getLastName());
    newAccount.setPassword(dto.getPassword());

    Account account = accountRepository.save(newAccount);
    account.setPassword(null);
    return Optional.of(account);
  }

  public Account authenticate(LoginForm dto) {
    Account acc = accountRepository.findByEmail(dto.getEmail())
        .orElseThrow(() -> new RuntimeException("Email không tồn tại!"));

    if (!dto.getPassword().equals(acc.getPassword())) {
      throw new RuntimeException("Mật khẩu không chính xác!");
    }

    // Trả về acc để Controller tự xử lý tiếp
    return acc;
  }

  public void saveAccountToCookie(Account account, HttpServletResponse response) {
    HttpCookie cookie = new HttpCookie("userId", account.getEmail());
    cookie.setPath("/");
    cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
    response.addHeader("Set-Cookie", cookie.toString());
  }
}
