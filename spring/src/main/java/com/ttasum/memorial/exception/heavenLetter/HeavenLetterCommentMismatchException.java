package com.ttasum.memorial.exception.heavenLetter;

public class HeavenLetterCommentMismatchException extends RuntimeException {
    public HeavenLetterCommentMismatchException() {
        super("편지 번호와 댓글이 일치하지 않습니다.");
    }
}
