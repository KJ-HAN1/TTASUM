package com.ttasum.memorial.dto.chat;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

import java.util.Map;

/**
 * 챗봇 응답에 사용할 DTO
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatApiResponse {
    private boolean success; // LLM 메시지 반환 여부
    private int code;        // 상태 코드
    private String message;  // 성공 시 응답 결과 메시지 및 실패 시 사용자에게 표시할 오류 메시지
    @Nullable private Map<String, String> data; // 챗봇 실제 응답 메시지

    private static final String SUCCESS_DEFAULT_MSG = "응답이 성공적으로 반환되었습니다";
    private static final String ERR_DEFAULT_MSG = "내부 네트워크 서버 오류, 타임 아웃 등으로 일시적인 문제가 발생했습니다. 잠시 후 다시 이용해 주세요.";

    // 성공적으로 반환했을 경우(데이터)
    public static ChatApiResponse ok(Map<String, String> data) {
        return ChatApiResponse.builder()
                .success(true)
                .code(HttpStatus.OK.value())
                .message(SUCCESS_DEFAULT_MSG)
                .data(data)
                .build();
    }

    // 스프링 입력 유효성 검사, 직렬화, 파이썬에서 발생한 에러 (지정 상태코드, 지정 메시지)
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
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ERR_DEFAULT_MSG)
                .data(null)
                .build();
    }
}
