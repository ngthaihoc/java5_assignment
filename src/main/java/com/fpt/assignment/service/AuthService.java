package com.fpt.assignment.service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpt.assignment.dto.LoginForm;
import com.fpt.assignment.dto.RegisterForm;
import com.fpt.assignment.entity.Account;
import com.fpt.assignment.entity.Cart;
import com.fpt.assignment.repository.AccountRepository;
import com.fpt.assignment.repository.CartRepository;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Service
public class AuthService {

    @Autowired
    HttpSession session;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    AccountRepository accountRepository;

    public Account getLoggedAccount() {
        return (Account) session.getAttribute("user");
    }

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

        Cart cart = new Cart();
        cart.setAccount(account);
        cartRepository.save(cart);

        return Optional.of(account);
    }

    public Account authenticate(LoginForm dto) {
        Account acc = accountRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại!"));

        if (!dto.getPassword().equals(acc.getPassword())) {
            throw new RuntimeException("Mật khẩu không chính xác!");
        }

        return acc;
    }

    public void saveAccountToCookie(Account account, HttpServletResponse response) {
        byte[] bytes = (account.getEmail()).getBytes();
        String userInfo = Base64.getEncoder().encodeToString(bytes);
        Cookie cookie = new Cookie("user", userInfo);
        cookie.setMaxAge(30 * 24 * 60 * 60); // hiệu lực 30 ngày
        cookie.setPath("/"); // hiệu lực toàn ứng dụng
        response.addCookie(cookie);
    }

    public void updatePassword(String email, String newPassword) {
        Account acc = accountRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email không tồn tại!"));

        acc.setPassword(newPassword);

        accountRepository.save(acc);

    }

    public boolean isEmailNotExisted(String email) {
        Optional<Account> existingAccount = accountRepository.findByEmail(email);
        return existingAccount.isEmpty();
    }
}
