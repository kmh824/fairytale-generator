package com.fairytale.fairytale_generator.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JWTAuthentication extends AbstractAuthenticationToken {

    private final Long userId;

    public JWTAuthentication(Long userId) {
        super(null);
        this.userId = userId;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }
}
