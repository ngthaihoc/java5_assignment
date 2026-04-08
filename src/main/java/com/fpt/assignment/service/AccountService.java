package com.fpt.assignment.service;

import com.fpt.assignment.dto.ChangePasswordRequest;
import com.fpt.assignment.entity.Account;
import com.fpt.assignment.repository.AccountRepository;
import com.fpt.assignment.utils.FileService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    FileService fileService;

    public Account updateAccount(String email, String fullname, MultipartFile photo, HttpSession session) throws IOException {
        Account account = accountRepository.findByEmail(email).orElse(null);

        if (photo != null && !photo.isEmpty()) {
            String fileName = fileService.saveFile(photo, "avatar"); // Lưu file xuống ổ cứng
            account.setAvatar(fileName); // Lưu tên file vào DB
        }

        account.setFullname(fullname);
        Account updated = accountRepository.save(account);

        return updated;
    }

    public Account getInfo(String email) {
        return  accountRepository.findByEmail(email).orElse(null);
    }

    public boolean changePassword(ChangePasswordRequest request) {
        Account account = accountRepository.findByEmail(request.email()).orElse(null);
        if (account == null) return false;

        if (!request.oldPassword().equals(account.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác!");
        }

        account.setPassword(request.newPassword());
        accountRepository.save(account);
        return true;
    }
}
