package com.fairytale.fairytale_generator.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtils {

    private final Key key;
    private final int jwtExpirationMs;

    public JWTUtils(@Value("${jwt.secret}") String jwtSecret,
                    @Value("${jwt.expirationMs}") int jwtExpirationMs) {
        System.out.println("JWT_SECRET: " + jwtSecret);  // 환경 변수 값 출력
        System.out.println("JWT_EXPIRATION_MS: " + jwtExpirationMs);  // 환경 변수 값 출력
        this.jwtExpirationMs = jwtExpirationMs;  // 만료 시간 설정
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());  // JWT Secret 키 생성
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
