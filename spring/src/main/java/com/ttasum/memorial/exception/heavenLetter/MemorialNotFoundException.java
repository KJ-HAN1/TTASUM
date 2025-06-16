package com.ttasum.memorial.exception.heavenLetter;

public class MemorialNotFoundException extends RuntimeException {
    public MemorialNotFoundException() {
        super("기증자 정보를 찾을 수 없습니다.");
    }
}