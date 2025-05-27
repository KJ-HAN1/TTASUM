package com.ttasum.memorial.dto.DonationStory;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class DonationStoryUpdateRequestDto {

    @Size(max = 10, message = "areaCode는 최대 10글자까지 가능합니다.")
    private String areaCode;

    @NotBlank(message = "제목은 공백일 수 없습니다.")
    @Size(max = 600, message = "제목은 최대 600글자까지 가능합니다.")
    private String storyTitle;

    @NotBlank(message = "기증자 이름은 공백일 수 없습니다.")
    @Size(max = 150, message = "기증자 이름은 최대 150글자까지 가능합니다.")
    private String donorName;

    @NotBlank(message = "패스워드는 최소4글자, 최대 60글자까지 가능합니다.")
    @Size(min = 4, max = 60, message = "패스워드는 최소4글자, 최대 60글자까지 가능합니다.")
    private String storyPasscode;

    @NotBlank(message = "작성자는 공백일 수 없습니다.")
    @Size(max = 150, message = "작성자는 최대 150글자까지 가능합니다.")
    private String storyWriter;

    @Pattern(regexp = "Y|N", message = "anonymityFlag는 'Y' 또는 'N'이어야 합니다.")
    private String anonymityFlag;

    @Min(value = 0, message = "readCount는 0 이상의 값이어야 합니다.")
    private Integer readCount;

    @NotBlank(message = "본문은 공백일 수 없습니다.")
    private String storyContents;

    @Size(max = 600, message = "fileName은 최대 600글자까지 가능합니다.")
    private String fileName;

    @Size(max = 600, message = "originalFileName은 최대 600글자까지 가능합니다.")
    private String orgFileName;

    @Size(max = 60, message = "writerId는 최대 60글자까지 가능합니다.")
    private String writerId;

    @Size(max = 60, message = "modifierId는 최대 60글자까지 가능합니다.")
    private String modifierId;
}
