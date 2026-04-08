package com.fpt.assignment.controller;

import com.fpt.assignment.dto.LoginForm;
import com.fpt.assignment.dto.RegisterForm;
import com.fpt.assignment.entity.Account;
import com.fpt.assignment.security.JwtService;
import com.fpt.assignment.service.AuthService;
import com.fpt.assignment.utils.XMailer;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    JwtService jwtService;

    @Autowired
    HttpSession session; // Vẫn dùng session cho OTP flow

    // ĐĂNG NHẬP → trả JWT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginForm dto) {
        try {
            Account user = authService.authenticate(dto);
            String token = jwtService.generateToken(user.getEmail());

            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "email", user.getEmail(),
                    "fullname", user.getFullname(),
                    "avatar", user.getAvatar() != null ? user.getAvatar() : "",
                    "isAdmin", user.isAdmin()
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }

    // ĐĂNG XUẤT (chỉ cần xóa token ở client)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "Đăng xuất thành công"));
    }

    // ĐĂNG KÝ → gửi OTP
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterForm dto) {
        if (!authService.isEmailNotExisted(dto.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email đã tồn tại"));
        }

        String otp = authService.generateOTP();
        session.setAttribute("otp", otp);
        session.setAttribute("registerDTO", dto);
        session.setAttribute("otpAction", "register");

        XMailer.send(dto.getEmail(), "Xác thực tài khoản", "Mã OTP: " + otp);
        return ResponseEntity.ok(Map.of("message", "OTP_SENT"));
    }

    // XÁC THỰC OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verify(@RequestParam String otpCode) {
        String serverOtp = (String) session.getAttribute("otp");

        if (serverOtp != null && serverOtp.equals(otpCode)) {
            String otpAction = (String) session.getAttribute("otpAction");

            if ("register".equals(otpAction)) {
                RegisterForm dto = (RegisterForm) session.getAttribute("registerDTO");
                Account account = authService.createAccount(dto).orElse(null);

                session.removeAttribute("otp");
                session.removeAttribute("registerDTO");
                session.removeAttribute("otpAction");

                // Trả JWT luôn sau khi đăng ký xong
                String token = jwtService.generateToken(account.getEmail());
                return ResponseEntity.ok(Map.of(
                        "message", "REGISTER_SUCCESS",
                        "token", token,
                        "email", account.getEmail(),
                        "fullname", account.getFullname()
                ));
            }

            if ("forgot".equals(otpAction)) {
                return ResponseEntity.ok(Map.of("message", "OTP_VALID_FOR_RESET"));
            }
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Mã OTP không chính xác"));
    }

    // QUÊN MẬT KHẨU
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        if (authService.isEmailNotExisted(email)) {
            return ResponseEntity.status(404).body(Map.of("message", "Email không tồn tại"));
        }

        String otp = authService.generateOTP();
        session.setAttribute("otp", otp);
        session.setAttribute("forgotEmail", email);
        session.setAttribute("otpAction", "forgot");

        XMailer.send(email, "Đổi mật khẩu tài khoản", "Mã OTP của bạn là: " + otp);
        return ResponseEntity.ok(Map.of("message", "OTP_SENT"));
    }

    // ĐỔI MẬT KHẨU (sau forgot)
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String newPassword) {
        String email = (String) session.getAttribute("forgotEmail");

        if (email == null) {
            return ResponseEntity.status(400).body(Map.of("message", "Phiên làm việc hết hạn"));
        }

        try {
            authService.updatePassword(email, newPassword);
            session.removeAttribute("otp");
            session.removeAttribute("forgotEmail");
            session.removeAttribute("otpAction");
            return ResponseEntity.ok(Map.of("message", "PASSWORD_CHANGED"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("message", "Lỗi khi cập nhật mật khẩu"));
        }
    }
}