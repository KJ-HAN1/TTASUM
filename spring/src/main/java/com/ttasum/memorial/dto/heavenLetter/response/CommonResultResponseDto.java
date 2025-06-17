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
public class CommonResultResponseDto {
    private int result;
    private String message;

    //성공
    public static CommonResultResponseDto success(String message) {
        return new CommonResultResponseDto(1, message);
    }

    //실패(false하면 오류 나는 이유 찾기)
    public static CommonResultResponseDto fail(String message) {
        return new CommonResultResponseDto(0, message);
    }
}