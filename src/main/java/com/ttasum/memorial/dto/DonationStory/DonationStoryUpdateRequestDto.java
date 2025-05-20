package com.ttasum.memorial.dto.DonationStory;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class DonationStoryUpdateRequestDto {

    @Size(max = 10, message = "areaCode는 최대 10글자까지 가능합니다.")
    private String areaCode;

    @NotBlank
    @Size(max = 600, message = "title은 최대 600글자까지 가능합니다.")
    private String title;

    @NotBlank @Size(max = 150, message = "donorName은 최대 150글자까지 가능합니다.")
    private String donorName;

    @NotBlank @Size(max = 60, message = "passcode는 최대 60글자까지 가능합니다.")
    private String passcode;

    @NotBlank @Size(max = 150, message = "writer는 최대 150글자까지 가능합니다.")
    private String writer;

    @Pattern(regexp = "Y|N", message = "anonymityFlag는 'Y' 또는 'N'이어야 합니다.")
    private String anonymityFlag;

    @Min(value = 0, message = "readCount는 0 이상의 값이어야 합니다.")
    private Integer readCount;

    @NotBlank
    private String contents;

    @Size(max = 600, message = "fileName은 최대 600글자까지 가능합니다.")
    private String fileName;

    @Size(max = 600, message = "originalFileName은 최대 600글자까지 가능합니다.")
    private String originalFileName;

    @Size(max = 60, message = "writerId는 최대 60글자까지 가능합니다.")
    private String writerId;

    @Size(max = 60, message = "modifierId는 최대 60글자까지 가능합니다.")
    private String modifierId;
}
