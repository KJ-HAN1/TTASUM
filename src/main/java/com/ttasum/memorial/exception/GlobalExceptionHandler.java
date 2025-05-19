// 전역 예외 처리기
package com.ttasum.memorial.exception;

import com.ttasum.memorial.exception.forbiddenWord.ForbiddenWordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ForbiddenWordException.class)
    public ResponseEntity<Map<String, Object>> handleForbiddenWordException(ForbiddenWordException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("code", 400);
        errorResponse.put("forbiddenPoint", e.getForbiddenPoint());
        errorResponse.put("message", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}
