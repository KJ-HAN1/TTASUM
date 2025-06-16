package com.ttasum.memorial.exception.recipientLetter;

public class RecipientLetterNotFoundException extends RuntimeException {
    public RecipientLetterNotFoundException() {
        super("해당 수혜자 편지를 찾을 수 없습니다.");
    }
}
