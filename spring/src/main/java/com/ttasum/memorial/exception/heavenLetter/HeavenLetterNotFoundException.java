package com.ttasum.memorial.exception.heavenLetter;

public class HeavenLetterNotFoundException extends RuntimeException {
    public HeavenLetterNotFoundException() {
        super("해당 하늘나라 편지를 찾을 수 없습니다.");
    }
}