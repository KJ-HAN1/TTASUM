package com.ttasum.memorial.exception.common.badRequest;

//비밀번호 인증 실패 시 발생하는 예외
public class InvalidPasscodeException extends BadRequestException {
    public InvalidPasscodeException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}
