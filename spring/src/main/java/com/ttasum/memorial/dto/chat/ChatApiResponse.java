package com.ttasum.memorial.dto.chat;

import lombok.*;

import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatApiResponse {
    private boolean success;
    private int code;
    private String message;
    private Map<String, String> data;

    private static final int SUCCESS_CODE = 200;
    private static final String SUCCESS_DEFAULT_MSG = "응답이 성공적으로 반환되었습니다";
    private static final int ERR_CODE = 500;
    private static final String ERR_DEFAULT_MSG = "내부 네트워크 서버 오류, 타임 아웃 등으로 일시적인 문제가 발생했습니다.";

    // 성공적으로 반환했을 경우(데이터)
    public static ChatApiResponse ok(Map<String, String> data) {
        return ChatApiResponse.builder()
                .success(true)
                .code(SUCCESS_CODE)
                .message(SUCCESS_DEFAULT_MSG)
                .data(data)
                .build();
    }

    // 스프링 입력 유효성 검사, 파이썬에서 발생한 에러 (지정 상태코드, 지정 메시지)
    public static ChatApiResponse error(int code, String message) {
        return ChatApiResponse.builder()
                .success(false)
                .code(code)
                .message(message)
                .data(null)
                .build();
    }

    // 그 외 발생한 에러 (기본 500, 기본 메시지)
    public static ChatApiResponse error() {
        return ChatApiResponse.builder()
                .success(false)
                .code(ERR_CODE)
                .message(ERR_DEFAULT_MSG)
                .data(null)
                .build();
    }
}
