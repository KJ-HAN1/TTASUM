package com.ttasum.memorial.dto.heavenLetter.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CommonCommentRequestDto {

    //등록
    @Getter
    @Setter
    public static class CreateCommentRequest {
        @NotNull(message = "편지 번호는 필수입니다.")
        private Integer letterSeq;

        @NotBlank(message = "댓글 작성자는 필수입니다.")
        private String commentWriter;

        @NotBlank(message = "댓글 비밀번호는 필수입니다.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
                message = "비밀번호는 영문과 숫자 포함 8자리 이상입니다"
        )
        private String commentPasscode;

        @NotBlank(message = "댓글 내용은 필수입니다.")
        private String contents;
    }

    //수정 인증
    @Getter
    @Setter
    public static class CommentVerifyRequest{

        @NotNull(message = "편지 번호는 필수입니다.")
        private Integer letterSeq;

        @NotNull(message = "댓글 번호는 필수입니다.")
        private Integer commentSeq;

        @NotBlank(message = "댓글 비밀번호는 필수입니다.")
        private String commentPasscode;
    }

    //수정
    @Getter
    @Setter
    public static class UpdateCommentRequest {

        @NotNull(message = "댓글 번호는 필수입니다.")
        private Integer commentSeq;

        @NotNull(message = "편지 번호는 필수입니다.")
        private Integer letterSeq;

        @NotBlank(message = "댓글 작성자는 필수입니다.")
        private String commentWriter;

        @NotBlank(message = "댓글 비밀번호는 필수입니다.")
        private String commentPasscode;

        @NotBlank(message = "댓글 내용은 필수입니다.")
        private String contents;

    }
}


