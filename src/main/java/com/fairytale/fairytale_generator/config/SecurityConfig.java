package com.fairytale.fairytale_generator.config;

import com.fairytale.fairytale_generator.service.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // CSRF 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/login", "/oauth2/**", "/api/auth/success").permitAll()  // 로그인, OAuth 경로 허용
                        .anyRequest().authenticated()  // 나머지 요청은 인증 필요
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/oauth2/authorization/google")  // 구글 OAuth 로그인 페이지로 직접 리디렉션
                        .defaultSuccessUrl("/api/auth/success", true)  // 로그인 성공 후 리디렉션할 URL
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService())  // 사용자 정보 처리 서비스 설정
                        )
                );

        return http.build();
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService();
    }
}
