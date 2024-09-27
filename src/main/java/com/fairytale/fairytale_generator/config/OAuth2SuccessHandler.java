package com.fairytale.fairytale_generator.config;

import com.fairytale.fairytale_generator.entity.User;
import com.fairytale.fairytale_generator.repository.UserRepository;
import com.fairytale.fairytale_generator.service.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JWTUtils jwtUtils;

    public OAuth2SuccessHandler(UserRepository userRepository, JWTUtils jwtUtils) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String googleId = oAuth2User.getAttribute("sub");
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // 사용자 정보가 데이터베이스에 있는지 확인하고 없으면 저장
        User user = userRepository.findByGoogleId(googleId).orElseGet(() -> {
            User newUser = new User();
            newUser.setGoogleId(googleId);
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setUpdatedAt(LocalDateTime.now());
            return newUser;
        });

        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // JWT 토큰 생성 - userId와 email을 함께 전달
        String jwtToken = jwtUtils.generateToken(user.getId(), email);

        // JWT 토큰을 URL 파라미터로 추가하여 프론트엔드로 리다이렉트
        response.sendRedirect("http://localhost:3000?token=" + jwtToken);
    }

}
