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
import com.ttasum.memorial.dto.ApiResponse;
import com.ttasum.memorial.exception.DonationStory.DonationStoryNotFoundException;
import com.ttasum.memorial.exception.CaptchaVerificationFailedException;
import com.ttasum.memorial.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 유효성 검사 실패 (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        if (msg.isEmpty()) {
            msg = "필수 입력값이 누락되었습니다.";
        }
        ApiResponse response = ApiResponse.badRequest(msg);
        return ResponseEntity.badRequest().body(response);
    }

    // ResponseStatusException 처리
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse> handleStatusException(ResponseStatusException ex) {
        ApiResponse response = ApiResponse.fail(ex.getStatus().value(), ex.getReason());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    // DonationStory 조회 실패 (404 Not Found)
    @ExceptionHandler(DonationStoryNotFoundException.class)
    public ResponseEntity<ApiResponse> handleDonationStoryNotFound(DonationStoryNotFoundException ex) {
        log.warn("스토리 조회 실패: {}", ex.getMessage());
        ApiResponse response = ApiResponse.fail(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // ResourceNotFoundException 처리 (404 Not Found)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("공지사항 조회 실패: {}", ex.getMessage());
        ApiResponse response = ApiResponse.fail(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // CAPTCHA 검증 실패 (400 Bad Request)
    @ExceptionHandler(CaptchaVerificationFailedException.class)
    public ResponseEntity<ApiResponse> handleCaptchaException(CaptchaVerificationFailedException ex) {
        ApiResponse response = ApiResponse.badRequest(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // 서버 내부 오류 (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAll(Exception ex) {
        log.error("서버 내부 오류", ex);
        ApiResponse response = ApiResponse.serverError();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
