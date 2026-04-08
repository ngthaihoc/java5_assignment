package com.fpt.assignment.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // Đổi secret này thành chuỗi dài hơn trong production
    private static final String SECRET = "mysecretkeymysecretkeymysecretkey12345";
    private static final long EXPIRATION_MS = 7 * 24 * 60 * 60 * 1000L; // 7 ngày

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    // Tạo token từ email
    public String generateToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(getKey())
                .compact();
    }

    // Lấy email từ token
    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    // Kiểm tra token còn hợp lệ không
    public boolean isValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}