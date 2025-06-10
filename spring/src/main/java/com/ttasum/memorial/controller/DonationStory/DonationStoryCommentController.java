package com.ttasum.memorial.controller.DonationStory;

import com.ttasum.memorial.dto.ApiResponse;
import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentCreateRequestDto;
import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentDeleteRequestDto;
import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentUpdateRequestDto;
import com.ttasum.memorial.service.DonationStory.DonationStoryCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/donationLetters")
public class DonationStoryCommentController {

    private final DonationStoryCommentService commentService;

    /**
     * 댓글 등록
     * @param storySeq 게시글 id
     * @param dto 댓글 등록 요청 dto
     */
    @PostMapping("/{storySeq}/comments")
    public ResponseEntity<ApiResponse> createComment(@PathVariable Integer storySeq, @RequestBody @Valid DonationStoryCommentCreateRequestDto dto) {
        log.info("POST /donationLetters/{}/comments - 댓글 등록 요청", storySeq);
        commentService.createComment(storySeq, dto);
        // 201 Created -> location 다음 동작(조회 등)을 위한 URI를 제공
//        int commentSeq = commentService.createComment(storySeq, dto);
//        URI location = URI.create(String.format("/donationLetters/%d/comments/%d", storySeq, commentSeq));
//        return ResponseEntity.created(location).build();
        return ResponseEntity.ok().body(ApiResponse.ok(
                HttpStatus.CREATED.value(),
                "편지 댓글이 성공적으로 등록되었습니다."
        ));
    }

    /**
     * 댓글 수정(비밀번호 검증 포함)
     * @param storySeq 게시글 id
     * @param commentSeq 댓글 id
     * @param dto 댓글 수정 요청 dto
     * @return 200 OK
     */
    @PatchMapping("/{storySeq}/comments/{commentSeq}")
    public ResponseEntity<ApiResponse> updateComment(@PathVariable Integer storySeq, @PathVariable Integer commentSeq, @RequestBody @Valid DonationStoryCommentUpdateRequestDto dto) {
        log.info("PUT /donationLetters/{}/comments/{} - 댓글 수정 요청", storySeq, commentSeq);
        commentService.updateComment(storySeq, commentSeq, dto);
        return ResponseEntity.ok().body(ApiResponse.ok(
                HttpStatus.OK.value(),
                "스토리 댓글이 성공적으로 수정되었습니다."
        ));
    }

    /**
     * 댓글 삭제(비밀번호 검증 포함)
     * @param storySeq 게시글 id
     * @param commentSeq 댓글 id
     * @param dto 댓글 삭제 요청 dto
     * @return 200 OK
     */
    @DeleteMapping("/{storySeq}/comments/{commentSeq}")
    public ResponseEntity<ApiResponse> deleteComment(
            @PathVariable Integer storySeq,
            @PathVariable Integer commentSeq,
            @Valid @RequestBody DonationStoryCommentDeleteRequestDto dto) {
        log.info("DELETE /donationLetters/{}/comments/{} - 댓글 삭제 요청", storySeq, commentSeq);
        commentService.softDeleteComment(storySeq, commentSeq, dto);
        return ResponseEntity.ok().body(ApiResponse.ok(
                HttpStatus.OK.value(),
                "스토리 댓글이 성공적으로 삭제되었습니다."
        ));
    }
}
