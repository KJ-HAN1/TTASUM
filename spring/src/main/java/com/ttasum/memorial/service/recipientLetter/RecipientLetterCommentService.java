package com.ttasum.memorial.service.recipientLetter;

import com.ttasum.memorial.dto.common.ApiResponse;
import com.ttasum.memorial.dto.recipientLetterComment.request.RecipientLetterCommentRequestDto;

public interface RecipientLetterCommentService {

    //댓글 등록
    void createComment(Integer pathLetterSeq, RecipientLetterCommentRequestDto createCommentRequest);
}