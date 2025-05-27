// 전역 예외 처리기
package com.ttasum.memorial.exception;

import com.ttasum.memorial.exception.blameText.BlamTextException;
import com.ttasum.memorial.exception.forbiddenWord.ForbiddenWordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ResponseStatusException 처리
    @ExceptionHandler(ResponseStatusException.class)
    public String handleStatusException(ResponseStatusException ex) {
        return ex.getReason();
    }

    // 비난 글, 금칙어 확인 예외 처리
    @ExceptionHandler({ForbiddenWordException.class, BlamTextException.class})
    public ResponseEntity<Map<String, Object>> handleForbiddenWordException(RuntimeException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("success", false);
        errorResponse.put("code", 400);
        errorResponse.put("message", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }
}
