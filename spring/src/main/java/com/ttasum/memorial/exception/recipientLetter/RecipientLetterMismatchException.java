package com.ttasum.memorial.exception.recipientLetter;

public class RecipientLetterMismatchException extends RuntimeException {
    public RecipientLetterMismatchException(

    ) {
        super("요청하신 편지 번호가 일치하지 않습니다");
    }
}
