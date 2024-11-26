// src/main/java/com/fairytale/fairytale_generator/exception/JsonParsingException.java
package com.fairytale.fairytale_generator.exception;

public class JsonParsingException extends RuntimeException {
    public JsonParsingException(String message) {
        super(message);
    }

    public JsonParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
