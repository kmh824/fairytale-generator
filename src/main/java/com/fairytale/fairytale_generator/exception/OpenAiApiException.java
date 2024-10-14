// src/main/java/com/fairytale/fairytale_generator/exception/OpenAiApiException.java
package com.fairytale.fairytale_generator.exception;

public class OpenAiApiException extends RuntimeException {
    public OpenAiApiException(String message) {
        super(message);
    }

    public OpenAiApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
