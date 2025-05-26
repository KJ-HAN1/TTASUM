package com.ttasum.memorial.dto.DonationStory;

import com.ttasum.memorial.domain.entity.DonationStory.DonationStory;
import com.ttasum.memorial.domain.entity.DonationStory.DonationStoryComment;
import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentResponseDto;
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
    private String title;
    private String donorName;
    private String writer;
    private String anonymityFlag;
    private Integer readCount;
    private String contents;
    private String fileName;
    private String originalFileName;
    private String writerId;
    private String modifierId;
    private LocalDateTime writeTime;
    private LocalDateTime modifyTime;

    // 댓글 목록
    private List<DonationStoryCommentResponseDto> comments;

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
                .comments(null) // 목록에서는 댓글 포함하지 않음
                .build();
    }


    /**
     * 엔티티 → DTO 변환
     * @param story DonationStory 엔티티
     * @param commentEntities 댓글 엔티티 목록
     * @return 변환된 DTO 객체
     */
    public static DonationStoryResponseDto fromEntity(DonationStory story, List<DonationStoryComment> commentEntities) {
        List<DonationStoryCommentResponseDto> commentDtos = commentEntities.stream()
                .map(comment -> DonationStoryCommentResponseDto.builder()
                        .id(comment.getCommentSeq())
                        .writer(comment.getWriter())
                        .contents(comment.getContents())
                        .writeTime(comment.getWriteTime())
                        .build())
                .collect(Collectors.toList());

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
                .comments(commentDtos)
                .build();
    }
}
