package com.ttasum.memorial.dto.DonationStoryComment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

// 댓글 수정 요청 dto
@Getter
@Setter
public class DonationStoryCommentUpdateRequestDto {

    @NotBlank
    @Size(max = 60)
    private String passcode;

    @NotBlank
    @Size(max = 3000)
    private String contents;
}
