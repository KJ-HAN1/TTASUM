package com.ttasum.memorial.dto.recipientLetter.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

//수정&인증
// 1: 성공, 0: 실패
public class RecipientLetterCommonResponseDto {
    private int result;
    private String message;

    //성공
    public static RecipientLetterCommonResponseDto success(String message) {
        return new RecipientLetterCommonResponseDto(1, message);
    }

    //실패(false하면 오류 나는 이유 찾기)
    public static RecipientLetterCommonResponseDto fail(String message) {
        return new RecipientLetterCommonResponseDto(0, message);
    }
}