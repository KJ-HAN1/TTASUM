package com.ttasum.memorial.exception;


import com.ttasum.memorial.dto.common.ApiResponse;
import com.ttasum.memorial.dto.heavenLetter.response.CommonResultResponseDto;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterCommentResponseDto;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterResponseDto;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterCommonResponseDto;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterResponseDto;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterUpdateResponseDto;
import com.ttasum.memorial.exception.common.Conflict.AlreadyDeletedException;
import com.ttasum.memorial.exception.common.badRequest.CaptchaVerificationFailedException;
import com.ttasum.memorial.exception.common.notFound.NotFoundException;
import com.ttasum.memorial.exception.donationStory.DonationStoryNotFoundException;
import com.ttasum.memorial.exception.heavenLetter.*;
import com.ttasum.memorial.exception.recipientLetter.RecipientLetterNotFoundException;
import lombok.extern.slf4j.Slf4j;
import com.ttasum.memorial.exception.common.badRequest.BadRequestException;
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
    public ResponseEntity<HeavenLetterResponseDto> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(HeavenLetterResponseDto.fail(400, e.getMessage()));
    }

//    //서버 오류
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<HeavenLetterResponseDto> handleException(Exception e) {
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body(HeavenLetterResponseDto.fail(500, "서버 내부 오류가 발생했습니다"));
//    }

    // HeavenLetter - 편지 조회 실패 (404 Not Found)
    @ExceptionHandler(HeavenLetterNotFoundException.class)
    public ResponseEntity<HeavenLetterResponseDto> handleLetterNotFound(HeavenLetterNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(HeavenLetterResponseDto.fail(404, ex.getMessage()));
    }

    // HeavenLetter - 비밀번호 인증 실패 (400 Bad Request)
    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<CommonResultResponseDto> handleInvalidPassword(InvalidPasswordException ex) {
        return ResponseEntity.badRequest()
                .body(CommonResultResponseDto.fail(ex.getMessage()));
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

    // HeavenLetter - 기증자 정보 없음 (404 Not Found)
    @ExceptionHandler(MemorialNotFoundException.class)
    public ResponseEntity<HeavenLetterResponseDto> handleMemorialNotFound(MemorialNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(HeavenLetterResponseDto.fail(404, ex.getMessage()));
    }

    //RecipientLetter - 편지 조회 실패 (404 Not Found)
    @ExceptionHandler(RecipientLetterNotFoundException.class)
    public ResponseEntity<RecipientLetterResponseDto> handleRecipientLetterNotFound(RecipientLetterNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(RecipientLetterResponseDto.fail(404, ex.getMessage()));
    }

    // 이미 삭제된 리소스 요청 (409 Conflict)
    @ExceptionHandler(AlreadyDeletedException.class)
    public ResponseEntity<ApiResponse> handleAlreadyDeleted(AlreadyDeletedException ex) {
        log.info("이미 삭제된 리소스 요청: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.conflict(ex.getMessage()));
    }

    //예측하지 못한 예외 처리 (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleUnexpectedException(Exception ex) {
        log.error("서버 내부 오류 발생: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.serverError());


    }

}