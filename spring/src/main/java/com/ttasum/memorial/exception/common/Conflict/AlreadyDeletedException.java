package com.ttasum.memorial.exception.common.Conflict;

// 이미 삭제된 리소스에 대한 요청 (409 Conflict)
public class AlreadyDeletedException extends RuntimeException {
    public AlreadyDeletedException() {
        super("이미 삭제된 편지입니다.");
    }
}
