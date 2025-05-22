package com.ttasum.memorial.dto.DonationStoryComment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

// 댓글 삭제 요청 dto
@Getter
@Setter
public class DonationStoryCommentDeleteRequestDto {

    @NotBlank
    @Size(max = 60)
    private String passcode;

}
