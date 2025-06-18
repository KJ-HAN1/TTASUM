package com.ttasum.memorial.dto.heavenLetter.response;

import lombok.*;

//수정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HeavenLetterUpdateResponsDto {

    private boolean success;
    private int code;
    private String message;


    // 수정 - 성공 201
    public static HeavenLetterUpdateResponsDto success() {
        return new HeavenLetterUpdateResponsDto(true, 201, "편지 수정이 성공적으로 되었습니다.");
    }

    // 수정 - 실패 400, 500
    public static HeavenLetterUpdateResponsDto fail(int code, String message) {
        return new HeavenLetterUpdateResponsDto(false, code, message);
    }
}