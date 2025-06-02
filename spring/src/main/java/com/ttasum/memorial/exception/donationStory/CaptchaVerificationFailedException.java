package com.ttasum.memorial.exception.donationStory;

import com.ttasum.memorial.exception.common.BadRequestException;

public class CaptchaVerificationFailedException extends BadRequestException {
    public CaptchaVerificationFailedException() {
        super("캡차 인증에 실패했습니다.");
    }
}
