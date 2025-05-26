// 전역 예외 처리기
package com.ttasum.memorial.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ResponseStatusException 처리
    @ExceptionHandler(ResponseStatusException.class)
    public String handleStatusException(ResponseStatusException ex) {
        return ex.getReason();
    }
}
