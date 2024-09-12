package com.fairytale.fairytale_generator.controller;



import com.fairytale.fairytale_generator.config.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/success")
    public String loginSuccess(@AuthenticationPrincipal OAuth2User oAuth2User) {
        // JWT 토큰 생성
        String token = jwtUtils.generateToken(oAuth2User.getAttribute("email"));
        return "JWT Token: " + token;
    }
}
