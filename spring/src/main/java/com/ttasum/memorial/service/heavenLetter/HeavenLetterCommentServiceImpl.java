package com.ttasum.memorial.service.heavenLetter;

import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetter;
import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetterComment;
import com.ttasum.memorial.domain.repository.heavenLetter.HeavenLetterCommentRepository;
import com.ttasum.memorial.domain.repository.heavenLetter.HeavenLetterRepository;
import com.ttasum.memorial.dto.heavenLetter.request.CommonCommentRequestDto;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterCommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor

public class HeavenLetterCommentServiceImpl implements HeavenLetterCommentService{

    private final HeavenLetterCommentRepository commentRepository;
    private final HeavenLetterRepository heavenLetterRepository;

    //댓글 등록
    @Transactional
    @Override
    public HeavenLetterCommentResponseDto createComment(CommonCommentRequestDto.CreateCommentRequest createCommentRequest) {
        HeavenLetter heavenLetter = heavenLetterRepository.findById(createCommentRequest.getLetterSeq()).get();

        HeavenLetterComment letterComment = HeavenLetterComment.builder()
                .letterSeq(heavenLetter)
                .commentWriter(createCommentRequest.getCommentWriter())
                .commentPasscode(createCommentRequest.getCommentPasscode())
                .contents(createCommentRequest.getContents())
                .build();

        commentRepository.save(letterComment);

        return HeavenLetterCommentResponseDto.success("편지 댓글이 성공적으로 등록되었습니다.");
    }
    //수정 인증(공통)
    @Transactional(readOnly = true)
    @Override
    public boolean verifyCommentPasscode(Integer commentSeq, String passcode) {
        HeavenLetterComment heavenLetterComment = commentRepository.findById(commentSeq).orElseThrow();
        return heavenLetterComment.getCommentPasscode().equals(passcode);
    }
    //
//    //댓글 수정
    @Transactional
    @Override
    public HeavenLetterCommentResponseDto updateComment(CommonCommentRequestDto.UpdateCommentRequest updateCommentRequest) {
        HeavenLetterComment heavenLetterComment = commentRepository.findById(updateCommentRequest.getCommentSeq()).get();

        heavenLetterComment.updateComment(updateCommentRequest);

        return HeavenLetterCommentResponseDto.success("편지 댓글이 성공적으로 수정되었습니다.");
    }
    //댓글 삭제
    @Transactional
    @Override
    public HeavenLetterCommentResponseDto.CommentVerifyResponse deleteComment(CommonCommentRequestDto.DeleteCommentRequest deleteCommentRequest) {

        if (!this.verifyCommentPasscode(deleteCommentRequest.getCommentSeq(), deleteCommentRequest.getCommentPasscode())) {
            return HeavenLetterCommentResponseDto.CommentVerifyResponse.fail("비밀번호가 일치하지 않습니다.");
        }
        HeavenLetterComment heavenLetterComment = commentRepository.findById(deleteCommentRequest.getCommentSeq()).get();
        //소프트 삭제 커멘드 사용
        heavenLetterComment.softDeleteComment();

        return HeavenLetterCommentResponseDto.CommentVerifyResponse.success("편지 댓글이 성공적으로 삭제되었습니다.");
    }
}

