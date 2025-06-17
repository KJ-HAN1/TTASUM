package com.ttasum.memorial.service.recipientLetter;

import com.ttasum.memorial.dto.common.ApiResponse;
import com.ttasum.memorial.dto.recipientLetterComment.request.RecipientLetterCommentRequestDto;
import com.ttasum.memorial.dto.recipientLetterComment.request.RecipientLetterCommentVerifyRequestDto;

public interface RecipientLetterCommentService {

    //댓글 등록
    void createComment(Integer pathLetterSeq, RecipientLetterCommentRequestDto createCommentRequest);

    //댓글 수정 인증(공통)
    boolean verifyCommentPasscode(RecipientLetterCommentVerifyRequestDto commentVerifyRequestDto, Integer commentSeq);

}