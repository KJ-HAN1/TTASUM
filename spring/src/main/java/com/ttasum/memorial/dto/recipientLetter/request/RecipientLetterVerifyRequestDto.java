package com.ttasum.memorial.dto.recipientLetter.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter

public class RecipientLetterVerifyRequestDto {

    @NotNull(message = "편지 번호는 필수입니다.")
    private Integer letterSeq;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String letterPasscode;
}

