package com.ttasum.memorial.exception.common.badRequest;

//요청한 URL과 BODY 편지의 번호가 맞지 않을 때 발생하는 예외
public class PathVariableMismatchException extends BadRequestException {
    public PathVariableMismatchException(String message) {
        super(message);
    }
}
