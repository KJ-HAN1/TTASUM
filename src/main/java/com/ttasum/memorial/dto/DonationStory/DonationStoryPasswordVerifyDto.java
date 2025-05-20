package com.ttasum.memorial.dto.DonationStory;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

// 기증후 스토리 비밀번호 확인 요청 dto
@Getter
@Setter
public class DonationStoryPasswordVerifyDto {
    /** 편지 비밀번호 (VARCHAR(60), NULL 허용) */
    @Size(max = 60, message = "passcode는 최대 60글자까지 가능합니다.")
    private String storyPasscode;
}
