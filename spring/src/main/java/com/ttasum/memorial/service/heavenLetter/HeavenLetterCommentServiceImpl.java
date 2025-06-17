package com.ttasum.memorial.service.heavenLetter;

import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetter;
import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetterComment;
import com.ttasum.memorial.domain.repository.heavenLetter.HeavenLetterCommentRepository;
import com.ttasum.memorial.domain.repository.heavenLetter.HeavenLetterRepository;
import com.ttasum.memorial.dto.heavenLetter.request.CommonCommentRequestDto;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterCommentResponseDto;
import com.ttasum.memorial.exception.heavenLetter.HeavenLetterCommentMismatchException;
import com.ttasum.memorial.exception.heavenLetter.InvalidPasswordException;
import com.ttasum.memorial.exception.heavenLetter.HeavenLetterNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import com.ttasum.memorial.exception.heavenLetter.HeavenLetterCommentNotFoundException;


@Service
@RequiredArgsConstructor

public class HeavenLetterCommentServiceImpl implements HeavenLetterCommentService{

    private final HeavenLetterCommentRepository commentRepository;
    private final HeavenLetterRepository heavenLetterRepository;

    //댓글 등록
    @Transactional
    @Override
    public HeavenLetterComment createComment(CommonCommentRequestDto.CreateCommentRequest createCommentRequest) {
        HeavenLetter heavenLetter = heavenLetterRepository.findById(createCommentRequest.getLetterSeq())
                .orElseThrow(HeavenLetterNotFoundException::new);

        HeavenLetterComment letterComment = HeavenLetterComment.builder()
                .letterSeq(heavenLetter)
                .commentWriter(createCommentRequest.getCommentWriter())
                .commentPasscode(createCommentRequest.getCommentPasscode())
                .contents(createCommentRequest.getContents())
                .build();

        return commentRepository.save(letterComment);
    }
    //수정 인증(공통)
    @Transactional(readOnly = true)
    @Override
    public boolean verifyCommentPasscode(Integer commentSeq, String passcode) {
        HeavenLetterComment heavenLetterComment = commentRepository.findById(commentSeq)
                .orElseThrow(HeavenLetterCommentNotFoundException::new);

        if (!heavenLetterComment.getCommentPasscode().equals(passcode)) {
            throw new InvalidPasswordException();
        }
        return true;
    }

    //댓글 수정
    @Transactional
    @Override
    public HeavenLetterComment updateComment(Integer commentSeq, Integer letterSeq, CommonCommentRequestDto.UpdateCommentRequest updateCommentRequest) {

        //댓글 존재 여부 확인
        HeavenLetterComment comment = commentRepository.findById(commentSeq)
                .orElseThrow(HeavenLetterCommentNotFoundException::new);

        //삭제된 댓글인지 확인
        if ("Y".equals(comment.getDelFlag())) {
            throw new HeavenLetterCommentNotFoundException();
        }

        //편지 번호 일치 여부 확인
        if (!comment.getLetterSeq().getId().equals(letterSeq)) {
            throw new HeavenLetterCommentMismatchException();
        }

        //댓글 수정
        //응답 반환
        return  comment.updateComment(updateCommentRequest);
    }

    //댓글 삭제
    @Transactional
    @Override
    public HeavenLetterCommentResponseDto.CommentVerifyResponse deleteComment(CommonCommentRequestDto.DeleteCommentRequest deleteCommentRequest) {

        this.verifyCommentPasscode(deleteCommentRequest.getCommentSeq(), deleteCommentRequest.getCommentPasscode());

        HeavenLetterComment heavenLetterComment = commentRepository.findById(deleteCommentRequest.getCommentSeq())
                .orElseThrow(HeavenLetterCommentNotFoundException::new);

        // 삭제 여부 확인
        if ("Y".equals(heavenLetterComment.getDelFlag())) {
            throw new HeavenLetterCommentNotFoundException();
        }

        //소프트 삭제 커멘드 사용
        heavenLetterComment.softDeleteComment();

        return HeavenLetterCommentResponseDto.CommentVerifyResponse.success("편지 댓글이 성공적으로 삭제되었습니다.");
    }
}
