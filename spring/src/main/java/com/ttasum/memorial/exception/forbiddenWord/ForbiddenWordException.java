package com.ttasum.memorial.exception.forbiddenWord;

import lombok.Getter;

@Getter
public class ForbiddenWordException extends RuntimeException{
    private final String forbiddenPoint;

    public ForbiddenWordException(String message, String forbiddenPoint) {
        super(message);
        this.forbiddenPoint = forbiddenPoint;
    }

}
