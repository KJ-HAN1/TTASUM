package com.ttasum.memorial.dto.DonationStory;

import com.ttasum.memorial.domain.entity.DonationStory.DonationStory;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 기증후 스토리 응답용 DTO
 * 필요한 필드만 노출하여 클라이언트에 예측 가능한 JSON 구조로 반환합니다.
 */
@Getter
@Builder
public class DonationStoryResponseDto {

    /** 스토리 일련번호 */
    private Integer storySeq;

    /** 제목 */
    private String title;

    /** 기증자 이름 */
    private String donorName;

    /** 작성자 */
    private String writer;

    /** 익명 여부 (“Y” 또는 “N”) */
    private String anonymityFlag;

    /** 조회 수 */
    private Integer readCount;

    /** 본문 내용 */
    private String contents;

    /** 저장된 파일명 */
    private String fileName;

    /** 원본 파일명 */
    private String originalFileName;

    /** 작성자 ID */
    private String writerId;

    /** 최종 수정자 ID */
    private String modifierId;

    /** 작성 시각 */
    private LocalDateTime writeTime;

    /** 수정 시각 */
    private LocalDateTime modifyTime;

    /**
     * 엔티티 → DTO 변환
     * @param story DonationStory 엔티티
     * @return 변환된 DTO 객체
     */
    public static DonationStoryResponseDto fromEntity(DonationStory story) {
        return DonationStoryResponseDto.builder()
                .storySeq(story.getId())
                .title(story.getTitle())
                .donorName(story.getDonorName())
                .writer(story.getWriter())
                .anonymityFlag(story.getAnonymityFlag())
                .readCount(story.getReadCount())
                .contents(story.getContents())
                .fileName(story.getFileName())
                .originalFileName(story.getOriginalFileName())
                .writerId(story.getWriterId())
                .modifierId(story.getModifierId())
                .writeTime(story.getWriteTime())
                .modifyTime(story.getModifyTime())
                .build();
    }
}
