package com.ttasum.memorial.service.heavenLetter;

import com.ttasum.memorial.dto.heavenLetter.request.CommonCommentRequestDto;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterCommentResponseDto;

public interface HeavenLetterCommentService {

    //댓글 등록
    HeavenLetterCommentResponseDto createComment(CommonCommentRequestDto.CreateCommentRequest createCommentRequest);

}
