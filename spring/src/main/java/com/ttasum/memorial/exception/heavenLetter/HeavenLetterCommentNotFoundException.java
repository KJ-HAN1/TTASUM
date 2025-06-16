package com.ttasum.memorial.exception.heavenLetter;

public class HeavenLetterCommentNotFoundException extends RuntimeException {
    public HeavenLetterCommentNotFoundException() {
            super("하늘나라 편지에 해당 댓글이 존재하지 않거나 삭제되었습니다.");
    }
}

