package com.ttasum.memorial.controller.heavenLetter;

import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetterComment;
import com.ttasum.memorial.dto.common.ApiResponse;
import com.ttasum.memorial.dto.heavenLetter.request.CommonCommentRequestDto;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterCommentResponseDto;
import com.ttasum.memorial.exception.heavenLetter.HeavenLetterCommentMismatchException;
import com.ttasum.memorial.service.blameText.BlameTextPersistenceService;
import com.ttasum.memorial.service.forbiddenWord.TestReviewService;
import com.ttasum.memorial.service.heavenLetter.HeavenLetterCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.ttasum.memorial.domain.type.BoardType.DONATION;
import static com.ttasum.memorial.domain.type.BoardType.HEAVEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/heavenLetters/{letterSeq}/comments")
public class HeavenLetterCommentController {

    private final HeavenLetterCommentService heavenLetterCommentService;
    private final TestReviewService testReviewService;
    private final BlameTextPersistenceService blameTextPersistenceService;

    //등록
    @PostMapping
    public ResponseEntity<?> createComment(
            @RequestBody @Valid CommonCommentRequestDto.CreateCommentRequest createCommentRequest) {
        HeavenLetterComment comment = heavenLetterCommentService.createComment(createCommentRequest);

        // 비난글 AI 필터링 추가
        testReviewService.saveCommentFromBlameTable(comment, true, HEAVEN.getType());

        return ResponseEntity.ok().body(ApiResponse.ok(
                HttpStatus.CREATED.value(),
                "편지 댓글이 성공적으로 등록되었습니다."
        ));    }

    // 수정 인증
    @PostMapping("/{commentSeq}/verifyPwd")
    public ResponseEntity<HeavenLetterCommentResponseDto.CommentVerifyResponse> verifyCommentPasscode(
            @PathVariable Integer commentSeq,
            @RequestBody @Valid CommonCommentRequestDto.CommentVerifyRequest commentVerifyRequest) {

        heavenLetterCommentService.verifyCommentPasscode(commentSeq, commentVerifyRequest.getCommentPasscode());

        return ResponseEntity.status(HttpStatus.OK).body(HeavenLetterCommentResponseDto.CommentVerifyResponse.success("비밀번호가 일치합니다."));
    }

    //댓글 수정
    @PatchMapping("/{commentSeq}")
    public ResponseEntity<?> updateComment(
            //값을 자바 변수로 맵핑
            @PathVariable Integer commentSeq,
            @RequestBody @Valid CommonCommentRequestDto.UpdateCommentRequest updateCommentRequest) {

        if (!commentSeq.equals(updateCommentRequest.getCommentSeq())) {
            throw new HeavenLetterCommentMismatchException();
        }

        // 실제 편지 번호는 본문에서 추출
        Integer letterSeq = updateCommentRequest.getLetterSeq();

        HeavenLetterComment comment = heavenLetterCommentService.updateComment(commentSeq, letterSeq, updateCommentRequest);
        // 비난글 AI 필터링 추가
        testReviewService.saveCommentFromBlameTable(comment, false, HEAVEN.getType());

        return ResponseEntity.ok().body(ApiResponse.ok(
                HttpStatus.OK.value(),
                "스토리 댓글이 성공적으로 수정되었습니다."
        ));
    }

    //댓글 삭제
    @DeleteMapping("/{commentSeq}")
    public ResponseEntity<HeavenLetterCommentResponseDto.CommentVerifyResponse> deleteComment(
            @PathVariable(name = "commentSeq") Integer commentSeq,
            @PathVariable(name = "letterSeq") Short letterSeq,
            @RequestBody CommonCommentRequestDto.DeleteCommentRequest deleteCommentRequest) {

        if (!commentSeq.equals(deleteCommentRequest.getCommentSeq())) {
            throw new HeavenLetterCommentMismatchException();
        }

        HeavenLetterCommentResponseDto.CommentVerifyResponse deleteCommentResponse = heavenLetterCommentService.deleteComment(deleteCommentRequest);

        blameTextPersistenceService.deleteBlameTextComment(letterSeq, commentSeq, HEAVEN.getType());
        // 결과에 따라 상태코드 분기
        if (deleteCommentResponse.getResult() == 1) {
            return ResponseEntity.ok(deleteCommentResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(deleteCommentResponse);
        }
    }
}
