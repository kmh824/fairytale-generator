package com.fairytale.fairytale_generator.controller;

import com.fairytale.fairytale_generator.service.JWTUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JWTUtils jwtUtils;

    public AuthController(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    // JWT 토큰 생성 (로그인 성공 후)
    @PostMapping("/success")
    public Map<String, String> loginSuccess(@RequestHeader("Authorization") String token) {
        // JWT 토큰 생성
        String jwtToken = jwtUtils.generateToken(token);

        // 반환 값으로 JSON 응답 (프론트엔드로 토큰 전달)
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);

        return response;  // Spring이 자동으로 JSON 변환
    }

    // JWT 토큰 검증
    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        String cleanedToken = token.replace("Bearer ", "");
        if (jwtUtils.validateToken(cleanedToken)) {
            String email = jwtUtils.getEmailFromToken(cleanedToken);
            return ResponseEntity.ok("Token is valid. User email: " + email);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
