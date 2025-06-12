package com.ttasum.memorial.dto.heavenLetter.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//수정
@Getter
@Setter

public class HeavenLetterUpdateRequestDto {

    @NotNull(message = "편지 번호는 필수입니다.")
    private Integer letterSeq;

    @NotBlank(message = "편지 작성자는 필수입니다.")
    private String letterWriter;

    private String anonymityFlag;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private String letterPasscode;

    private String donorName;

    private Integer donateSeq;

    @NotBlank(message = "지역 코드는 필수입니다.")
    private String areaCode;

    @NotBlank(message = "편지 제목은 필수입니다.")
    private String letterTitle;

    @NotBlank(message = "편지 내용은 필수입니다.")
    private String letterContents;

    private String orgFileName;

}
