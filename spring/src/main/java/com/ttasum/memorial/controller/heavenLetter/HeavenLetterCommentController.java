package com.ttasum.memorial.controller.heavenLetter;

import com.ttasum.memorial.dto.heavenLetter.request.CommonCommentRequestDto;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterCommentResponseDto;
import com.ttasum.memorial.exception.heavenLetter.HeavenLetterCommentMismatchException;
import com.ttasum.memorial.service.heavenLetter.HeavenLetterCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/heavenLetters/{letterSeq}/comments")
public class HeavenLetterCommentController {

    private final HeavenLetterCommentService heavenLetterCommentService;

    //등록
    @PostMapping
    public ResponseEntity<HeavenLetterCommentResponseDto> createComment(
            @PathVariable("letterSeq") int letterSeq,
            @RequestBody @Valid CommonCommentRequestDto.CreateCommentRequest createCommentRequest) {
        HeavenLetterCommentResponseDto createCommentResponse = heavenLetterCommentService.createComment(createCommentRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createCommentResponse);
    }

    // 수정 인증
    @PostMapping("/{commentSeq}/verifyPwd")
    public ResponseEntity<HeavenLetterCommentResponseDto.CommentVerifyResponse> verifyCommentPasscode(
            @PathVariable("letterSeq") int letterSeq,
            @PathVariable Integer commentSeq,
            @RequestBody @Valid CommonCommentRequestDto.CommentVerifyRequest commentVerifyRequest) {

        heavenLetterCommentService.verifyCommentPasscode(commentSeq, commentVerifyRequest.getCommentPasscode());

        return ResponseEntity.status(HttpStatus.OK).body(HeavenLetterCommentResponseDto.CommentVerifyResponse.success("비밀번호가 일치합니다."));
    }

    //댓글 수정
    @PatchMapping("/{commentSeq}")
    public ResponseEntity<HeavenLetterCommentResponseDto> updateComment(
            //값을 자바 변수로 맵핑
            @PathVariable("letterSeq") int letterSeq,
            @PathVariable Integer commentSeq,
            @RequestBody @Valid CommonCommentRequestDto.UpdateCommentRequest updateCommentRequest) {

        if (!commentSeq.equals(updateCommentRequest.getCommentSeq())) {
            throw new HeavenLetterCommentMismatchException();
        }

        // 실제 편지 번호는 본문에서 추출
//        Integer letterSeq = updateCommentRequest.getLetterSeq();

        HeavenLetterCommentResponseDto updateCommentResponse = heavenLetterCommentService.updateComment(commentSeq, letterSeq, updateCommentRequest);

        return ResponseEntity.status(HttpStatus.OK).body(updateCommentResponse);
    }

    //댓글 삭제
    @DeleteMapping("/{commentSeq}")
    public ResponseEntity<HeavenLetterCommentResponseDto.CommentVerifyResponse> deleteComment(
            @PathVariable("letterSeq") int letterSeq,
            @PathVariable Integer commentSeq,
            @RequestBody CommonCommentRequestDto.DeleteCommentRequest deleteCommentRequest) {

        if (!commentSeq.equals(deleteCommentRequest.getCommentSeq())) {
            throw new HeavenLetterCommentMismatchException();
        }

        HeavenLetterCommentResponseDto.CommentVerifyResponse deleteCommentResponse = heavenLetterCommentService.deleteComment(deleteCommentRequest);

        // 결과에 따라 상태코드 분기
        if (deleteCommentResponse.getResult() == 1) {
            return ResponseEntity.ok(deleteCommentResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(deleteCommentResponse);
        }
    }
}
