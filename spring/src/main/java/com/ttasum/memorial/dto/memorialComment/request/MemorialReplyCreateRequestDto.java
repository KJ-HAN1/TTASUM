package com.ttasum.memorial.dto.memorialComment.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

// 댓글 생성 요청용 DTO
@Getter
@Setter
public class MemorialReplyCreateRequestDto {

    @NotBlank(message = "댓글 내용을 입력해야 합니다.")
    private String replyContents;
}
