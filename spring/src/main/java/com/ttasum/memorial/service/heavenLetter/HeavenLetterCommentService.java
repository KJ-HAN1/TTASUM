package com.ttasum.memorial.service.heavenLetter;

import com.ttasum.memorial.dto.heavenLetter.request.CommonCommentRequestDto;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterCommentResponseDto;

public interface HeavenLetterCommentService {

    //댓글 등록
    HeavenLetterCommentResponseDto createComment(CommonCommentRequestDto.CreateCommentRequest createCommentRequest);

    //댓글 수정 인증(공통)
    boolean verifyCommentPasscode(Integer commentSeq, String passcode);

    //댓글 수정
    HeavenLetterCommentResponseDto updateComment(CommonCommentRequestDto.UpdateCommentRequest updateCommentRequest);

    //댓글 삭제
    HeavenLetterCommentResponseDto.CommentVerifyResponse deleteComment(CommonCommentRequestDto.DeleteCommentRequest deleteCommentRequest);
}
