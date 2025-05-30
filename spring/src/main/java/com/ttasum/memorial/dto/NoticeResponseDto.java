package com.ttasum.memorial.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ttasum.memorial.domain.entity.Notice;

import java.time.LocalDateTime;

// 공지사항 응답용 DTO
public record NoticeResponseDto(
        String boardCode,
        Integer articleSeq,
        String title,
        String contents,
        Integer readCount,
        String fixFlag,
        @JsonFormat(pattern = "yyyy-MM-dd a h:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime writeTime,
        String writerId
) {
    public static NoticeResponseDto of(Notice entity) {
        return new NoticeResponseDto(
                entity.getArticleId().getBoardCode(),
                entity.getArticleId().getArticleSeq(),
                entity.getTitle(),
                entity.getContents(),
                entity.getReadCount(),
                entity.getFixFlag(),
                entity.getWriteTime(),
                entity.getWriterId()
        );
    }
}

