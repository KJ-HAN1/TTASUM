package com.ttasum.memorial.dto.heavenLetter.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//수정
@Getter
@Setter

public class HeavenLetterUpdateRequestDto {

    @NotNull(message = "필수 입력값이 누락되었습니다.")
    private Integer letterSeq;

    @NotBlank(message = "필수 입력값이 누락되었습니다.")
    private String letterWriter;

    private String anonymityFlag;

    @NotBlank(message = "필수 입력값이 누락되었습니다.")
    private String letterPasscode;

    private String donorName;

    private Integer donateSeq;

    @NotBlank(message = "필수 입력값이 누락되었습니다.")
    private String areaCode;

    @NotBlank(message = "필수 입력값이 누락되었습니다.")
    private String letterTitle;

    @NotBlank(message = "필수 입력값이 누락되었습니다.")
    private String letterContents;

    private String orgFileName;

    private String fileName;

}
