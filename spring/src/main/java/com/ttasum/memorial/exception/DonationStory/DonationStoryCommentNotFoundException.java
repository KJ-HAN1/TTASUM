package com.ttasum.memorial.exception.DonationStory;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DonationStoryCommentNotFoundException extends RuntimeException {
    public DonationStoryCommentNotFoundException(Integer commentSeq) {
        super("댓글을 찾을 수 없습니다. ID=" + commentSeq);}
}
