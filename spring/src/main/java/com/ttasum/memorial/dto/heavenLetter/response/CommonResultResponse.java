package com.ttasum.memorial.dto.heavenLetter.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

// 1: 성공, 0: 실패
public class CommonResultResponse {
    private int result;
    private String message;

    //성공
    public static CommonResultResponse success(String message) {
        return new CommonResultResponse(1, message);
    }

    //실패(false하면 오류 나는 이유 찾기)
    public static CommonResultResponse fail(String message) {
        return new CommonResultResponse(0, message);
    }
}