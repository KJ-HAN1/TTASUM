package com.ttasum.memorial.dto.recipientLetter.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipientLetterUpdateResponseDto {

    private boolean success;
    private int code;
    private String message;

    //성공
    public static RecipientLetterUpdateResponseDto success() {
        return new RecipientLetterUpdateResponseDto(true,201,"편지 수정이 성공적으로 되었습니다.");
    }
    //실패
    public static RecipientLetterUpdateResponseDto fail(int code, String message){
        return new RecipientLetterUpdateResponseDto(false, code, message);
    }
}
