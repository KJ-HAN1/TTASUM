package com.ttasum.memorial.exception.common.Conflict;

//최소 삭제 이후 재삭제 시도 시 발생하는 예외 (409 Conflict)
public class AlreadyDeletedException extends RuntimeException {
    public AlreadyDeletedException() {
        super("이미 삭제된 편지입니다.");
    }
}
