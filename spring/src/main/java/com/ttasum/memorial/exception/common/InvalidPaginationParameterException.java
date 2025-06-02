package com.ttasum.memorial.exception.common;

public class InvalidPaginationParameterException extends BadRequestException {
    public InvalidPaginationParameterException (String message) {
        super(message);
    }
}
