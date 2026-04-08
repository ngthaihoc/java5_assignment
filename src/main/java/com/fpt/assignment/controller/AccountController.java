package com.fpt.assignment.controller;

import com.fpt.assignment.dto.ChangePasswordRequest;
import com.fpt.assignment.entity.Account;
import com.fpt.assignment.service.AccountService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class AccountController {

    @Autowired
    AccountService accountService;

    @GetMapping("/me")
    public ResponseEntity<Account> me(@RequestParam String email) {
        Account account = accountService.getInfo(email);

        if (account == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(account);
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateAccount(
            @RequestParam("fullname") String fullname,
            @RequestParam("email") String email,
            @RequestParam(value = "photo", required = false) MultipartFile photo,
            HttpSession session) throws IOException {

        Account account = accountService.updateAccount(email, fullname, photo, session);

        if (account == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(account);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request) {
        try {
            boolean isChanged = accountService.changePassword(request);
            if (isChanged) {
                return ResponseEntity.ok(Map.of("message", "Đổi mật khẩu thành công!"));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Không tìm thấy người dùng"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", e.getMessage()));
        }
    }
}