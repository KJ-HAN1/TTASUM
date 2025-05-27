// 전역 예외 처리기
package com.ttasum.memorial.exception;

import com.ttasum.memorial.dto.ApiResponse;
import com.ttasum.memorial.exception.DonationStory.DonationStoryNotFoundException;
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

        // 메시지가 없으면 기본 문구
        if (msg.isEmpty()) {
            msg = "필수 입력값이 누락되었습니다.";
        }
        return ResponseEntity.badRequest().body(ApiResponse.badRequest(msg));
    }

    // 서버 내부 오류 (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleAll(Exception ex) {
        ApiResponse response = new ApiResponse(
                false,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "서버 내부 오류가 발생했습니다."
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

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

    // 공지사항 조회 중 ResourceNotFoundException 처리
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoticeNotFound(ResourceNotFoundException ex) {
        log.warn("공지사항 조회 실패: {}", ex.getMessage());
        ErrorResponse body = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(404).body(body);
    }

    // 공통 에러 응답 DTO
    public static class ErrorResponse {
        private final String code;
        private final String message;

        public ErrorResponse(String code, String message) {
            this.code    = code;
            this.message = message;
        }
        public String getCode()    { return code; }
        public String getMessage() { return message; }
    }
}
