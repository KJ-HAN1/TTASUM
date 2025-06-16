package com.ttasum.memorial.dto.donationStory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ttasum.memorial.domain.entity.donationStory.DonationStory;
import com.ttasum.memorial.domain.entity.donationStory.DonationStoryComment;
import com.ttasum.memorial.dto.donationStoryComment.DonationStoryCommentResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 기증후 스토리 응답용 DTO
 * 필요한 필드만 노출하여 클라이언트에 예측 가능한 JSON 구조로 반환합니다.
 */
@Getter
@Builder
public class DonationStoryResponseDto {

    private Integer storySeq;
    private String anonymityFlag;
    private String areaCode;
    private String storyTitle;
    private String storyPasscode;
    private String storyWriter;
    private String storyContents;
    private Integer readCount;
    private String fileName;
    private String orgFileName;
    // JSON 직렬화 시 포맷 지정 (예: "2020-02-04 오후 1:11:10")
    @JsonFormat(pattern = "yyyy-MM-dd a h:mm:ss", locale = "ko")
    private LocalDateTime writeTime;
    @JsonFormat(pattern = "yyyy-MM-dd a h:mm:ss", locale = "ko")
    private LocalDateTime modifyTime;
    private String modifierId;
    private String delFlag;

    private String donorName;
    private String writerId;
    // 댓글 목록
    private List<DonationStoryCommentResponseDto> comments;

    public static DonationStoryResponseDto fromEntity(DonationStory entity) {
        return DonationStoryResponseDto.builder()
                .storySeq(entity.getId())
                .anonymityFlag(entity.getAnonymityFlag())
                .areaCode(entity.getAreaCode())
                .storyTitle(entity.getTitle())
                .storyPasscode(entity.getPasscode())
                .storyWriter(entity.getWriter())
                .storyContents(entity.getContents())
                .readCount(entity.getReadCount())
                .fileName(entity.getFileName())
                .orgFileName(entity.getOriginalFileName())
                .writeTime(entity.getWriteTime())
                .writerId(entity.getWriterId())
                .modifyTime(entity.getModifyTime())
                .modifierId(entity.getModifierId())
                .delFlag(entity.getDelFlag())
                .donorName(entity.getDonorName())
                .comments(null) // 목록에서는 댓글 포함하지 않음
                .build();
    }


    /**
     * 엔티티 → DTO 변환
     * @param entity DonationStory 엔티티
     * @param commentEntities 댓글 엔티티 목록
     * @return 변환된 DTO 객체
     */
    public static DonationStoryResponseDto fromEntity(DonationStory entity, List<DonationStoryComment> commentEntities) {
        List<DonationStoryCommentResponseDto> commentDtos = commentEntities.stream()
                .map(comment -> DonationStoryCommentResponseDto.builder()
                        .id(comment.getCommentSeq())
                        .writer(comment.getWriter())
                        .contents(comment.getContents())
                        .writeTime(comment.getWriteTime())
                        .build())
                .collect(Collectors.toList());

        return DonationStoryResponseDto.builder()
                .storySeq(entity.getId())
                .anonymityFlag(entity.getAnonymityFlag())
                .areaCode(entity.getAreaCode())
                .storyTitle(entity.getTitle())
                .storyPasscode(entity.getPasscode())
                .storyWriter(entity.getWriter())
                .storyContents(entity.getContents())
                .readCount(entity.getReadCount())
                .fileName(entity.getFileName())
                .orgFileName(entity.getOriginalFileName())
                .writeTime(entity.getWriteTime())
                .writerId(entity.getWriterId())
                .modifyTime(entity.getModifyTime())
                .modifierId(entity.getModifierId())
                .delFlag(entity.getDelFlag())
                .donorName(entity.getDonorName())
                .comments(commentDtos)
                .build();
    }
}
