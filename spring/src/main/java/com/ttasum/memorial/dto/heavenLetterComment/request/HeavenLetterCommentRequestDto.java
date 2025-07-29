package com.ttasum.memorial.dto.heavenLetterComment.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
//등록
@Getter
@Setter

public class HeavenLetterCommentRequestDto {

        @NotNull(message = "필수 입력값이 누락되었습니다.")
        private Integer letterSeq;

        @NotBlank(message = "필수 입력값이 누락되었습니다.")
        private String commentWriter;

        @NotBlank(message = "필수 입력값이 누락되었습니다.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                message = "비밀번호는 영문과 숫자 포함 8자리 이상입니다"
        )
        private String commentPasscode;

        @NotBlank(message = "필수 입력값이 누락되었습니다.")
        private String contents;

        //    @NotBlank(message = "캡차 토큰이 필요합니다.")
        private String captchaToken;

}


