package com.ttasum.memorial.dto.DonationStory;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class DonationStoryDeleteRequestDto {

    @Size(max = 60, message = "modifierId는 최대 60글자까지 가능합니다.")
    private String modifierId;
}
