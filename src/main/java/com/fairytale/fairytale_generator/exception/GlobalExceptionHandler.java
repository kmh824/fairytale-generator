// src/main/java/com/fairytale/fairytale_generator/exception/GlobalExceptionHandler.java
package com.fairytale.fairytale_generator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<String> handleUnauthorizedAccessException(UnauthorizedAccessException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(FairyTaleNotFoundException.class)
    public ResponseEntity<String> handleFairyTaleNotFoundException(FairyTaleNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 다른 예외 처리도 필요할 경우 추가 가능
}
