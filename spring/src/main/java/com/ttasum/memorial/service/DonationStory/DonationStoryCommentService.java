package com.ttasum.memorial.service.DonationStory;

import com.ttasum.memorial.domain.entity.DonationStory.DonationStory;
import com.ttasum.memorial.domain.entity.DonationStory.DonationStoryComment;
import com.ttasum.memorial.domain.repository.DonationStory.DonationStoryCommentRepository;
import com.ttasum.memorial.domain.repository.DonationStory.DonationStoryRepository;
import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentCreateRequestDto;
import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentDeleteRequestDto;
import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentResponseDto;
import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentUpdateRequestDto;
import com.ttasum.memorial.exception.DonationStory.DonationStoryCommentNotFoundException;
import com.ttasum.memorial.exception.DonationStory.DonationStoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonationStoryCommentService {

    private final DonationStoryRepository storyRepository;
    private final DonationStoryCommentRepository commentRepository;


    /**
     * 댓글 등록
     * @param storySeq 스토리 id
     * @param dto 댓글 등록 요청 dto
     * @return 댓글 id 반환
     */
    @Transactional
    public int createComment(Integer storySeq, DonationStoryCommentCreateRequestDto dto) {
        DonationStory story = storyRepository.findByIdAndDelFlag(storySeq, "N")
                .orElseThrow(() -> new DonationStoryNotFoundException(storySeq));

        DonationStoryComment comment = DonationStoryComment.builder()
                .story(story)
                .writer(dto.getCommentWriter())
                .passcode(dto.getCommentPasscode())
                .contents(dto.getContents())
                .writerId(null) // 로그인 연동 시 변경
                .modifierId(null) // 수정자도 임시 값 적용
                .build();

        return commentRepository.save(comment).getCommentSeq();
    }

    /**
     * 스토리 댓글 목록 조회
     * @param storySeq 스토리 id
     * @return 스토리 해당 댓글들 dto 반환
     */
    @Transactional(readOnly = true)
    public List<DonationStoryCommentResponseDto> getComments(Integer storySeq) {
        // 스토리 id로 게시글 찾고
        List<DonationStoryComment> comments = commentRepository.findByStory_IdAndDelFlagOrderByWriteTimeAsc(storySeq, "N");

        // 해당 게시글의 댓글들을 dto형태로 변환 후 리스트에 저장
        List<DonationStoryCommentResponseDto> dtos = new ArrayList<>();
        for (DonationStoryComment comment : comments) {
            DonationStoryCommentResponseDto dto = DonationStoryCommentResponseDto.builder()
                    .id(comment.getCommentSeq())
                    .writer(comment.getWriter())
                    .contents(comment.getContents())
                    .writeTime(comment.getWriteTime())
                    .build();
            dtos.add(dto);
        }
        return dtos;
    }

    /**
     * 댓글 수정 + 비밀번호 검증
     * @param commentSeq 댓글 id
     * @param dto 댓글 수정 요청 dto
     */
    @Transactional
    public void updateComment(Integer storySeq, Integer commentSeq, DonationStoryCommentUpdateRequestDto dto) {
        DonationStoryComment comment = commentRepository.findByStory_IdAndCommentSeqAndDelFlag(storySeq, commentSeq,"N")
                .orElseThrow(() -> new DonationStoryCommentNotFoundException(commentSeq));

        // String modifierId = getUserIdFromToken(request); // 비회원이면 anonymous 반환
        comment.updateIfPasscodeMatches(dto.getCommentPasscode(), dto.getContents(),null); // 로그인 연동 시 수정자 ID로 교체
    }

    // eGov 환경 -> Spring Security + JWT 필터 사용
//    public String getLoginUserId() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//
//        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
//            return "anonymous";
//        }
//
//        return auth.getName();
//    }

    /**
     * 댓글 소프트 삭제 + 비밀번호 검증
     * @param commentSeq 댓글 id
     * @param dto 댓글 삭제 요청 dto
     */
    @Transactional
    public void softDeleteComment(Integer storySeq, Integer commentSeq, DonationStoryCommentDeleteRequestDto dto) {
        DonationStoryComment comment = commentRepository.findByStory_IdAndCommentSeqAndDelFlag(storySeq, commentSeq,"N")
                .orElseThrow(() -> new DonationStoryCommentNotFoundException(commentSeq));

        comment.deleteIfPasscodeMatches(dto.getCommentPasscode(),null);
    }


}