package com.example.printinfo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("잘못된 인자 요청: {}", ex.getMessage());
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", ex.getMessage());
        // 클라이언트에게는 '잘못된 요청'임을 알리는 HTTP 400 상태 코드를 반환합니다.
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 필요에 따라 다른 종류의 예외 핸들러를 여기에 추가할 수 있습니다.
}