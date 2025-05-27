package com.ttasum.memorial.dto.DonationStoryComment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

// 댓글 수정 요청 dto
@Getter
@Setter
public class DonationStoryCommentUpdateRequestDto {

    @NotBlank(message = "작성자는 공백일 수 없습니다.")
    @Size(max = 150, message = "작성자는 최대 150자까지 입력할 수 있습니다.")
    private String commentWriter;

    @NotBlank(message = "패스워드는 최소 4자, 최대 8자까지 가능합니다.")
    @Size(max = 60, message = "패스워드는 최소 4자, 최대 8자까지 가능합니다.")
    private String commentPasscode;

    @NotBlank(message = "내용은 공백일 수 없습니다.")
    @Size(max = 3000, message = "내용은 최대 150자까지 입력할 수 있습니다.")
    private String contents;
}
