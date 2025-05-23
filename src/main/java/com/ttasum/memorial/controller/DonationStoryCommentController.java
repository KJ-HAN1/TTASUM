package com.ttasum.memorial.controller;

import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentCreateRequestDto;
import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentDeleteRequestDto;
import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentUpdateRequestDto;
import com.ttasum.memorial.service.DonationStoryCommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
     * @return 200 OK
     */
    @PostMapping("/{storySeq}/comments")
    public ResponseEntity<Void> createComment(@PathVariable Integer storySeq, @RequestBody @Valid DonationStoryCommentCreateRequestDto dto) {
        log.info("POST /donationLetters/{}/comments - 댓글 등록 요청", storySeq);
        commentService.createComment(storySeq, dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 수정(비밀번호 검증 포함)
     * @param storySeq 게시글 id
     * @param commentSeq 댓글 id
     * @param dto 댓글 수정 요청 dto
     * @return 200 OK
     */
    @PutMapping("/{storySeq}/comments/{commentSeq}")
    public ResponseEntity<Void> updateComment(@PathVariable Integer storySeq, @PathVariable Integer commentSeq, @RequestBody @Valid DonationStoryCommentUpdateRequestDto dto) {
        log.info("PUT /donationLetters/{}/comments/{} - 댓글 수정 요청", storySeq, commentSeq);
        commentService.updateComment(commentSeq, dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 댓글 삭제(비밀번호 검증 포함)
     * @param storySeq 게시글 id
     * @param commentSeq 댓글 id
     * @param dto 댓글 삭제 요청 dto
     * @return 200 OK
     */
    @DeleteMapping("/{storySeq}/comments/{commentSeq}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Integer storySeq,
            @PathVariable Integer commentSeq,
            @Valid @RequestBody DonationStoryCommentDeleteRequestDto dto) {
        log.info("DELETE /donationLetters/{}/comments/{} - 댓글 삭제 요청", storySeq, commentSeq);
        commentService.softDeleteComment(commentSeq, dto);
        return ResponseEntity.ok().build();
    }
}
