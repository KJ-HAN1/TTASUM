// 전역 예외 처리기
package com.ttasum.memorial.exception;


import com.ttasum.memorial.dto.common.ApiResponse;

import com.ttasum.memorial.exception.common.badRequest.BadRequestException;
import com.ttasum.memorial.exception.common.conflict.AlreadyDeletedException;
import com.ttasum.memorial.exception.common.notFound.NotFoundException;

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
    // 잘못된 검색 필드 등 BadRequest 계열
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse> handleBadRequest(BadRequestException ex) {
        log.info("잘못된 요청: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(ApiResponse.badRequest(ex.getMessage()));
    }

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

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFound(NotFoundException ex) {
        log.info("리소스를 찾을 수 없음: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.notFound(ex.getMessage()));
    }

    // ResponseStatusException 처리
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse> handleStatusException(ResponseStatusException ex) {
        ApiResponse response = ApiResponse.fail(ex.getStatus().value(), ex.getReason());
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    // 이미 삭제된 리소스 요청 (409 Conflict)
    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<ApiResponse> handleAlreadyDeleted(AlreadyDeletedException ex) {
        log.info("이미 삭제된 리소스 요청: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.conflict(ex.getMessage()));
    }

    //서버 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception ex) {
        log.error("예기치 못한 서버 내부 오류가 발생했습니다.", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.serverError());
    }
}
