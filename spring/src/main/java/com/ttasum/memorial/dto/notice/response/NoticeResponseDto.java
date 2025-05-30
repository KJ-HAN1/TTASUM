package com.ttasum.memorial.dto.notice.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ttasum.memorial.domain.entity.notice.Notice;

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
    /**
     * 엔티티 -> DTO 변환 메서드
     * @param entity 공지사항 엔티티
     * @return 반환된 dto
     */
    public static NoticeResponseDto fromEntity(Notice entity) {
        return new NoticeResponseDto(
                entity.getNoticeId().getBoardCode(),
                entity.getNoticeId().getArticleSeq(),
                entity.getTitle(),
                entity.getContents(),
                entity.getReadCount(),
                "Y".equalsIgnoreCase(entity.getFixFlag()),
                entity.getWriteTime().toLocalDate(),
                entity.getWriteTime().isAfter(LocalDateTime.now().minusDays(3)),
                entity.getWriterId()
        );
    }
}

