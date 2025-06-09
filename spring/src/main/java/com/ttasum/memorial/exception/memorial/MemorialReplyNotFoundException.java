package com.ttasum.memorial.exception.memorial;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MemorialReplyNotFoundException extends RuntimeException {
    public MemorialReplyNotFoundException(Integer replySeq) {
        super("댓글을 찾을 수 없습니다. replySeq=" + replySeq);
    }
}
