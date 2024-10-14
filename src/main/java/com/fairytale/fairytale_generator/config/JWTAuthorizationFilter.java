package com.fairytale.fairytale_generator.config;

import com.fairytale.fairytale_generator.service.JWTUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;

    public JWTAuthorizationFilter(JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtUtils.validateToken(token)) {
                // Token에서 필요한 정보 추출
                Long userId = jwtUtils.getUserIdFromToken(token);
                String email = jwtUtils.getEmailFromToken(token);

                // Security context에 사용자 정보 추가
                JWTAuthentication authentication = new JWTAuthentication(userId);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // 유효하지 않은 토큰일 경우, 응답을 반환하고 체인을 종료
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}
