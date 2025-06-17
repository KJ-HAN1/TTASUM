package com.ttasum.memorial.controller.recipientLetter;

import com.ttasum.memorial.dto.common.ApiResponse;
import com.ttasum.memorial.dto.heavenLetter.request.CommonCommentRequestDto;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterCommentResponseDto;
import com.ttasum.memorial.dto.recipientLetterComment.request.RecipientLetterCommentRequestDto;
import com.ttasum.memorial.service.heavenLetter.HeavenLetterCommentService;
import com.ttasum.memorial.service.recipientLetter.RecipientLetterCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;



@RestController
@RequiredArgsConstructor
@RequestMapping("/recipientLetters/{letterSeq}/comments")

public class RecipientLetterCommentController {

    private final RecipientLetterCommentService recipientLetterCommentService;

    //등록
    @PostMapping
    public ResponseEntity<ApiResponse> createComment(
            @PathVariable Integer letterSeq,
            @RequestBody @Valid RecipientLetterCommentRequestDto createCommentRequest) {

        recipientLetterCommentService.createComment(letterSeq, createCommentRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(HttpStatus.CREATED.value(), "편지 댓글이 성공적으로 등록되었습니다."));
    }
}
