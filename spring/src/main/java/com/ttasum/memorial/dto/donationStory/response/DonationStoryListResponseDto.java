package com.ttasum.memorial.dto.donationStory.response;

import com.ttasum.memorial.domain.entity.donationStory.DonationStory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class DonationStoryListResponseDto {
    private Integer storySeq;       // 스토리 PK
    private String storyTitle;      // 제목
    private String storyWriter;     // 작성자(코디네이터)
    private String donorName;       // 기증자 명
    private String areaCode;        // 권역 코드
    private Integer readCount;      // 조회수
    private LocalDateTime writeTime;// 작성일시
    private Integer commentCount;   // 댓글 수

    private static String truncate(String title) {
        return (title != null && title.length() > 20)
                ? title.substring(0, 20) + "…"
                : title;
    }

    private static String maskName(String name) {
        if (name == null || name.length() < 2) return name;
        return name.charAt(0) + "*" + name.substring(2);
    }

    private static String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
    }

    public static DonationStoryListResponseDto fromEntity(DonationStory entity, int commentCount) {
        return DonationStoryListResponseDto.builder()
                .storySeq(entity.getId())
                .storyTitle(truncate(entity.getTitle()))
                .storyWriter(maskName(entity.getWriter()))
                .donorName(entity.getDonorName())
                .areaCode(entity.getAreaCode())
                .readCount(entity.getReadCount())
                .writeTime(entity.getWriteTime())
                .commentCount(commentCount)
                .build();
    }

}
