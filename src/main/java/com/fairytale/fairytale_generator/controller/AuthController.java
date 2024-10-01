package com.fairytale.fairytale_generator.controller;

import com.fairytale.fairytale_generator.service.JWTUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JWTUtils jwtUtils;

    public AuthController(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    // JWT 토큰 검증 (로그인 상태 확인)
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        String cleanedToken = token.replace("Bearer ", "");
        if (jwtUtils.validateToken(cleanedToken)) {
            String email = jwtUtils.getEmailFromToken(cleanedToken);
            Long userId = jwtUtils.getUserIdFromToken(cleanedToken);
            return ResponseEntity.ok("Token is valid. User ID: " + userId + ", Email: " + email);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}