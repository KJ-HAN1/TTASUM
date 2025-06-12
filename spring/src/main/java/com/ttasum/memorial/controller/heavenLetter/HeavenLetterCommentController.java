package com.ttasum.memorial.controller.heavenLetter;

import com.ttasum.memorial.dto.heavenLetter.request.CommonCommentRequestDto;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterCommentResponseDto;
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
            @RequestBody @Valid CommonCommentRequestDto.CreateCommentRequest createCommentRequest){
        HeavenLetterCommentResponseDto createCommentResponse = heavenLetterCommentService.createComment(createCommentRequest);

        //상태코드 분기 처리
        HttpStatus status;

        //등록 성공
        if(createCommentResponse.getCode() == 201){
            status = HttpStatus.CREATED;
        }
        //등록 실패
        else if (createCommentResponse.getCode() == 400) {
            status = HttpStatus.BAD_REQUEST;
        }else if (createCommentResponse.getCode() == 500) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }else{
            status = HttpStatus.BAD_REQUEST;
        }
        return ResponseEntity.status(status).body(createCommentResponse);
    }
    // 수정 인증
    @PostMapping("/{commentSeq}/verifyPwd")
    public ResponseEntity<HeavenLetterCommentResponseDto.CommentVerifyResponse> verifyCommentPasscode(
            @PathVariable Integer commentSeq,
            @RequestBody @Valid CommonCommentRequestDto.CommentVerifyRequest commentVerifyRequest) {
//
//        commentVerifyRequest.setCommentSeq(commentSeq);
        //verify -> verified 확인
        boolean verified = heavenLetterCommentService.verifyCommentPasscode(
                commentSeq , commentVerifyRequest.getCommentPasscode());

        // 위의 결과에 따른 bad response
        if(!verified){
            return ResponseEntity.badRequest().body(HeavenLetterCommentResponseDto.CommentVerifyResponse.fail("비밀번호가 일치하지 않습니다."));
        }
        return ResponseEntity.status(HttpStatus.OK).body(HeavenLetterCommentResponseDto.CommentVerifyResponse.success("비밀번호가 일치합니다."));
    }
    //댓글 수정
    @PatchMapping ("/{commentSeq}")
    public ResponseEntity<HeavenLetterCommentResponseDto> updateComment(
            //값을 자바 변수로 맵핑
            @PathVariable Integer commentSeq,
            @RequestBody @Valid CommonCommentRequestDto.UpdateCommentRequest updateCommentRequest) {

        //commentSeq 값을 요청 DTO에 직접 주입
        updateCommentRequest.setCommentSeq(commentSeq);

        HeavenLetterCommentResponseDto updateCommentResponse = heavenLetterCommentService.updateComment(updateCommentRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(updateCommentResponse);
    }
    //댓글 삭제
    @DeleteMapping("/{commentSeq}")
    public ResponseEntity<HeavenLetterCommentResponseDto.CommentVerifyResponse> deleteComment(
            @RequestBody CommonCommentRequestDto.DeleteCommentRequest deleteCommentRequest) {

        HeavenLetterCommentResponseDto.CommentVerifyResponse deleteCommentResponse = heavenLetterCommentService.deleteComment(deleteCommentRequest);

        // 결과에 따라 상태코드 분기
        if (deleteCommentResponse.getResult() == 1) {
            return ResponseEntity.ok(deleteCommentResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(deleteCommentResponse);
        }
    }
}
