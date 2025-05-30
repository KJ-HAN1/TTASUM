package com.ttasum.memorial.exception.donationStory;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DonationStoryNotFoundException extends RuntimeException {
    public DonationStoryNotFoundException(Integer storySeq) {
        super("스토리를 찾을 수 없습니다. ID=" + storySeq);
    }
}

