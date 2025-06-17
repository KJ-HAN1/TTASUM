package com.ttasum.memorial.service.recipientLetter;

import com.ttasum.memorial.domain.entity.recipientLetter.RecipientLetter;
import com.ttasum.memorial.domain.entity.recipientLetter.RecipientLetterComment;
import com.ttasum.memorial.domain.repository.recipientLetter.RecipientLetterCommentRepository;
import com.ttasum.memorial.domain.repository.recipientLetter.RecipientLetterRepository;
import com.ttasum.memorial.dto.common.ApiResponse;
import com.ttasum.memorial.dto.recipientLetterComment.request.RecipientLetterCommentRequestDto;
import com.ttasum.memorial.exception.common.Conflict.AlreadyDeletedException;
import com.ttasum.memorial.exception.common.badRequest.PathVariableMismatchException;
import com.ttasum.memorial.exception.common.notFound.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor

public class RecipientLetterCommentServiceImpl implements RecipientLetterCommentService {


    private final RecipientLetterRepository recipientLetterRepository;
    private final RecipientLetterCommentRepository recipientLetterCommentRepository;

    //등록
    @Transactional
    @Override
    public void createComment(Integer letterSeq, RecipientLetterCommentRequestDto createCommentRequest) {

        // 경로와 본문 값 불일치 (400 Bad Request)
        if(!letterSeq.equals(createCommentRequest.getLetterSeq())) {
            throw new PathVariableMismatchException("수혜자 편지 번호와 요청 번호가 일치하지 않습니다.");
        }

        // 리소스를 찾을 수 없음 (404 Not Found)
        RecipientLetter recipientLetter = recipientLetterRepository.findById(
                createCommentRequest.getLetterSeq()
        ).orElseThrow(() ->
                new NotFoundException("해당 수혜자 편지를 찾을 수 없습니다."));

        // 이미 삭제된 리소스에 대한 요청 (409 Conflict)
        if ("Y".equals(recipientLetter.getDelFlag())) {
            throw new AlreadyDeletedException();
        }

            RecipientLetterComment letterComment = RecipientLetterComment.builder()
                    .letterSeq(recipientLetter)
                    .commentWriter(createCommentRequest.getCommentWriter())
                    .commentPasscode(createCommentRequest.getCommentPasscode())
                    .contents(createCommentRequest.getContents())
                    .build();

            recipientLetterCommentRepository.save(letterComment);
    }
}
