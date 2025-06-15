package com.ttasum.memorial.exception;

import com.ttasum.memorial.dto.ApiResponse;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterCommentResponseDto;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterResponseDto;
import com.ttasum.memorial.exception.DonationStory.DonationStoryNotFoundException;
import com.ttasum.memorial.exception.heavenLetter.HeavenLetterCommentMismatchException;
import com.ttasum.memorial.exception.heavenLetter.HeavenLetterCommentNotFoundException;
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

    //유효성 검증 실패(400)
    //@ExceptionHandler : 지정한 예외가 발생했을 때 메서드 자동 호출
    //ResponseEntity<CommonResponse<Void>> : 응답 객체 형식
    //CommonResponse<Void>: 우리가 만든 공통 응답 구조. Void는 data가 없다는 뜻
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterResponse> handleValidationException(MethodArgumentNotValidException e){
//        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterResponse.fail(400,message));
//    }
    //잘못된 값 전달(비밀번호)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HeavenLetterResponseDto> handleIllegalArgumentException(IllegalArgumentException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(HeavenLetterResponseDto.fail(400,e.getMessage()));
    }

    //서버 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HeavenLetterResponseDto> handleException(Exception e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(HeavenLetterResponseDto.fail(500,"서버 내부 오류가 발생했습니다"));
    }
    // HeavenLetter - 댓글과 편지 번호 불일치 (409 Conflict)
    @ExceptionHandler(HeavenLetterCommentMismatchException.class)
    public ResponseEntity<HeavenLetterCommentResponseDto> handleCommentMismatch(HeavenLetterCommentMismatchException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(HeavenLetterCommentResponseDto.fail(409, ex.getMessage()));
    }
    // HeavenLetter - 해당 댓글 없음 (404 Conflict)
    @ExceptionHandler(HeavenLetterCommentNotFoundException.class)
    public ResponseEntity<HeavenLetterCommentResponseDto> handleCommentNotFound(HeavenLetterCommentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(HeavenLetterCommentResponseDto.fail(404, ex.getMessage()));
    }

}

