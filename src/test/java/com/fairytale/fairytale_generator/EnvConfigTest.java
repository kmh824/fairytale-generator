package com.fairytale.fairytale_generator;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvConfigTest {

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;

    @Value("${GOOGLE_CLIENT_SECRET}")
    private String googleClientSecret;

    @PostConstruct
    public void postConstruct() {
        System.out.println("GOOGLE_CLIENT_ID: " + googleClientId);
        System.out.println("GOOGLE_CLIENT_SECRET: " + googleClientSecret);
    }
}
