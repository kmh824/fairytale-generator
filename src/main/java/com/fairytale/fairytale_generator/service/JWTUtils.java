package com.fairytale.fairytale_generator.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtils {

    // JWT 키를 반환하는 메서드 추가
    @Getter
    private final Key key;
    private final int jwtExpirationMs;

    // @Value("${jwt.secret}") 제거
    public JWTUtils(@Value("${jwt.secret}") String secretKey, @Value("${jwt.expirationMs}") int jwtExpirationMs) {
        this.jwtExpirationMs = jwtExpirationMs;
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes()); // 환경변수에서 가져온 키를 사용
        System.out.println("JWT_SECRET: " + secretKey);
    }


    // JWT 토큰 생성
    public String generateToken(Long userId, String email) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId) // userId 추가
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.get("userId", Long.class);
    }

    // JWT 토큰에서 이메일 추출
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // JWT 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Expired JWT Token: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT Token: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Malformed JWT Token: " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("Invalid JWT Signature: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal argument token: " + e.getMessage());
        }
        return false;
    }
}
