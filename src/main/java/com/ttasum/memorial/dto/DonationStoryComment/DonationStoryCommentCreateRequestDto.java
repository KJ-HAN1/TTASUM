package com.ttasum.memorial.dto.DonationStoryComment;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

// 댓글 등록 요청 dto
@Getter
@Setter
public class DonationStoryCommentCreateRequestDto {

    @NotBlank
    @Size(max = 150, message = "작성자명은 150자 이하로 입력해주세요.")
    private String writer;

    @NotBlank
    @Size(max = 60, message = "비밀번호는 60자 이하로 입력해주세요.")
    private String passcode;

    @NotBlank
    @Size(max = 3000, message = "댓글 내용은 3000자 이하로 입력해주세요.")
    private String contents;

}
