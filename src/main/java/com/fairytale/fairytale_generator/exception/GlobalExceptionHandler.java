// GlobalExceptionHandler.java
package com.fairytale.fairytale_generator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // OpenAiApiException에 대한 처리
    @ExceptionHandler(OpenAiApiException.class)
    public ResponseEntity<String> handleOpenAiApiException(OpenAiApiException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("OpenAI API 호출에 실패했습니다: " + ex.getMessage());
    }

    // JsonParsingException에 대한 처리
    @ExceptionHandler(JsonParsingException.class)
    public ResponseEntity<String> handleJsonParsingException(JsonParsingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JSON 파싱 오류가 발생했습니다: " + ex.getMessage());
    }

    // 기타 일반적인 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다: " + ex.getMessage());
    }
}
