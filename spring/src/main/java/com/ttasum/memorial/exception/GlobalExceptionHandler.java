// 전역 예외 처리기
package com.ttasum.memorial.exception;

import com.ttasum.memorial.dto.blameText.ResponseDto;
import com.ttasum.memorial.exception.blameText.*;
import com.ttasum.memorial.exception.forbiddenWord.ForbiddenWordException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.jpa.JpaSystemException;
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleInvalidJsonRequestException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(
                ResponseDto.badRequest("error", HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage()));
    }

    // 비난 글, 금칙어 확인 예외 처리
    @ExceptionHandler({ForbiddenWordException.class,
            BlamTextException.class,
            MissingSentenceException.class,
            InvalidBlameTextRequestException.class})
    public ResponseEntity<?> handleForbiddenWordException(RuntimeException e) {
        return ResponseEntity.badRequest().body(
                ResponseDto.badRequest("error", HttpStatus.BAD_REQUEST.value(),
                        e.getMessage()));
    }

    @ExceptionHandler({ExternalServerConnectionException.class,
            ExternalServerTimeoutException.class,
            BlameTextApiServerException.class})
    public ResponseEntity<?> handleExternalServerConnectionException(RuntimeException e) {
        return ResponseEntity.internalServerError().body(
                ResponseDto.badRequest("error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        e.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleIntegrityViolation(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body(
                ResponseDto.badRequest("error", HttpStatus.BAD_REQUEST.value(),
                        "제약 조건 위반: 중복 또는 null 값 오류가 발생했습니다."));
    }

    @ExceptionHandler(JpaSystemException.class)
    public ResponseEntity<?> handleJpaError(JpaSystemException e) {
        return ResponseEntity.status(500).body(
                ResponseDto.badRequest("error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "JPA 처리 중 시스템 오류 발생했습니다."));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDataAccess(DataAccessException e) {
        return ResponseEntity.status(500).body(
                ResponseDto.badRequest("error", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "데이터베이스 접근 중 오류가 발생했습니다."));
    }

    @ExceptionHandler(InvalidJsonRequestException.class)
    public ResponseEntity<?> handleInvalidJsonRequestException(InvalidJsonRequestException e) {
        return ResponseEntity.badRequest().body(
                ResponseDto.badRequest("error", HttpStatus.BAD_REQUEST.value(), e.getMessage())
        );
    }
}
