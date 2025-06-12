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
}
