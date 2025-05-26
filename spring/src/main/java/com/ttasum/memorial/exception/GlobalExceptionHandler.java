// 전역 예외 처리기
package com.ttasum.memorial.exception;

import com.ttasum.memorial.exception.DonationStory.DonationStoryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ResponseStatusException 처리
    @ExceptionHandler(ResponseStatusException.class)
    public String handleStatusException(ResponseStatusException ex) {
        return ex.getReason();
    }

    @ExceptionHandler(DonationStoryNotFoundException.class)
    public ResponseEntity<Void> handleNotFound(DonationStoryNotFoundException ex) {
        log.warn("스토리 조회 실패: {}", ex.getMessage());
        return ResponseEntity.notFound().build();
    }
}
