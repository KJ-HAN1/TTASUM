package com.ttasum.memorial.dto.recipientLetter.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class RecipientLetterResponseDto {

    //등록
    private boolean success;
    private int code;
    private String message;

    //등록 - 성공 201
    public static RecipientLetterResponseDto success(){

        return new RecipientLetterResponseDto(true, 201, "편지가 성공적으로 등록되었습니다.");
    }
    //등록 - 실패 400, 500
    public static RecipientLetterResponseDto fail(int code, String message){
        return new RecipientLetterResponseDto(false,code,message);
    }

}
