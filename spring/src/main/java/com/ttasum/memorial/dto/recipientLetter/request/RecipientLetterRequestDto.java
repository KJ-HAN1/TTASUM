package com.ttasum.memorial.dto.recipientLetter.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


//등록
@Getter
@Setter

public class RecipientLetterRequestDto {

    @NotBlank(message = "필수 입력값이 누락되었습니다.")
    private String letterWriter;

    private String anonymityFlag;

    @NotBlank(message = "필수 입력값이 누락되었습니다")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "비밀번호는 영문과 숫자 포함 8자리 이상입니다"
    )
    private String letterPasscode;

    // 장기 코드 (ex. ORGAN001)
    private String organCode;

    private String organEtc;

    // 수혜 연도
    private String recipientYear;

    @NotBlank(message = "필수 입력값이 누락되었습니다.")
    private String storyTitle;

    @NotBlank(message = "필수 입력값이 누락되었습니다.")
    private String letterContents;

    // 첨부 파일 이름
    private String orgFileName;

    private String fileName;

    private String writerId;

}
