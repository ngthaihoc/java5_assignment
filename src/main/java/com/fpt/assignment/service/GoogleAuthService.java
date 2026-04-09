package com.fpt.assignment.service;

import com.fpt.assignment.entity.Account;
import com.fpt.assignment.entity.Cart;
import com.fpt.assignment.repository.AccountRepository;
import com.fpt.assignment.repository.CartRepository;
import com.fpt.assignment.security.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Service
public class GoogleAuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private JwtService jwtService;

    public Map<String, Object> verifyAndLogin(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(clientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String pictureUrl = (String) payload.get("picture");

                Account account = accountRepository.findById(email).orElse(null);
                if (account == null) {
                    account = new Account();
                    account.setEmail(email);
                    account.setFullname(name != null ? name : email);

                    if (pictureUrl != null && !pictureUrl.isEmpty()) {
                        try {
                            String uploadDir = "C:/uploads/avatar/";
                            File dir = new File(uploadDir);
                            if (!dir.exists()) dir.mkdirs();

                            String fileName = "google_" + UUID.randomUUID().toString() + ".jpg";
                            try (InputStream in = new URL(pictureUrl).openStream()) {
                                Files.copy(in, Paths.get(uploadDir + fileName), StandardCopyOption.REPLACE_EXISTING);
                                account.setAvatar(fileName);
                            }
                        } catch (Exception e) {
                            System.err.println("Failed to download Google avatar: " + e.getMessage());
                            account.setAvatar(null);
                        }
                    } else {
                        account.setAvatar(null);
                    }

                    account.setAdmin(false);
                    account.setEnabled(true);
                    account.setPassword(UUID.randomUUID().toString()); // Password for Google users
                    
                    accountRepository.save(account);

                    // Create Cart for new user (same as AuthService)
                    Cart cart = new Cart();
                    cart.setAccount(account);
                    cartRepository.save(cart);
                }

                String customJwt = jwtService.generateToken(account.getEmail());

                return Map.of(
                        "message", "GOOGLE_LOGIN_SUCCESS",
                        "token", customJwt,
                        "email", account.getEmail(),
                        "fullname", account.getFullname(),
                        "avatar", account.getAvatar() != null ? account.getAvatar() : "",
                        "isAdmin", account.isAdmin()
                );
            } else {
                throw new RuntimeException("Google ID Token is invalid");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify Google ID Token: " + e.getMessage());
        }
    }
}
