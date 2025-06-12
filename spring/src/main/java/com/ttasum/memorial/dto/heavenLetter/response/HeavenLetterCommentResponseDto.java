package com.ttasum.memorial.dto.heavenLetter.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class HeavenLetterCommentResponseDto {

    // 등록
    private boolean success;
    private int code;
    private String message;
    //등록 - 성공 201
    public static HeavenLetterCommentResponseDto success(String message) {
        return new HeavenLetterCommentResponseDto(true, 201, message);
    }
    //등록 - 실패 400, 500
    public static HeavenLetterCommentResponseDto fail(int code, String message) {
        return new HeavenLetterCommentResponseDto(false, code, message);
    }

}
