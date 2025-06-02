package com.ttasum.memorial.exception.common;

public class InvalidSearchFieldException extends BadRequestException {
    public InvalidSearchFieldException(String message) {
        super(message);
    }
}
