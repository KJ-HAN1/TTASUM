package com.ttasum.memorial.exception.donationStory;

public class CaptchaVerificationFailedException extends RuntimeException{
    public CaptchaVerificationFailedException() {
        super("캡차 인증에 실패했습니다.");
    }
}
