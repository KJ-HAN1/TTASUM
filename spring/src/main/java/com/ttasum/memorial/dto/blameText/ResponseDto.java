package com.ttasum.memorial.dto.blameText;

import lombok.Builder;

// factory 패턴
// 사용자에게 전달할 응답 dto
@Builder
public class ResponseDto {
    private String status;
    private String message;
    private int code;

    public static ResponseDto badRequest(String status, int code, String message) {
        return ResponseDto.builder()
                .status(status)
                .code(code)
                .message(message)
                .build();
    }

    public static ResponseDto ok(String status, int code, String message) {
        return ResponseDto.builder()
                .status(status)
                .code(code)
                .message(message)
                .build();
    }
}
