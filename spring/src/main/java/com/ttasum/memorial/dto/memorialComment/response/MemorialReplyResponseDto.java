package com.ttasum.memorial.dto.memorialComment.response;

import com.ttasum.memorial.domain.entity.memorial.MemorialReply;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

// 댓글 정보를 반환할 때 사용하는 DTO
@Getter
@AllArgsConstructor
public class MemorialReplyResponseDto {

    private Integer replySeq;
    private String replyContents;
    private LocalDateTime replyWriteTime;

    // 엔티티 -> DTO 변환
    public static MemorialReplyResponseDto of(MemorialReply reply) {
        return new MemorialReplyResponseDto(
                reply.getReplySeq(),
                reply.getReplyContents(),
                reply.getReplyWriteTime()
        );
    }
}
