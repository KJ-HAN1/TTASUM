package com.ttasum.memorial.dto.heavenLetter.request;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//수정 인증
@Getter
@Setter

public class HeavenLetterVerifyRequest{

    @NotNull(message = "편지 번호는 필수입니다.")
    private Integer letterSeq;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String letterPasscode;
}
