package com.ttasum.memorial.dto.DonationStory;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

// 기증후 스토리 비밀번호 확인 요청 dto
@Getter
@Setter
public class DonationStoryPasswordVerifyDto {
    @NotBlank(message = "패스워드는 필수 입력값입니다.")
    @Size(min = 8, max = 8, message = "비밀번호는 8자리여야 합니다.")
    @Pattern(
            regexp = "(?=.*[A-Za-z]).{8}",
            message = "비밀번호에 영문자를 최소 한 글자 포함해야 합니다."
    )
    private String storyPasscode;
}
