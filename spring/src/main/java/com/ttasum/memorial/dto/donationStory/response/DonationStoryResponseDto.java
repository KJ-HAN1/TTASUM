package com.ttasum.memorial.dto.donationStory.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ttasum.memorial.annotation.MaskNameIfAnonymous;
import com.ttasum.memorial.domain.entity.donationStory.DonationStory;
import com.ttasum.memorial.domain.entity.donationStory.DonationStoryComment;
import com.ttasum.memorial.dto.donationStoryComment.response.DonationStoryCommentResponseDto;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 기증후 스토리 상세 응답 DTO
 */
@Getter
@Builder
public class DonationStoryResponseDto {

    private Integer storySeq;
    private String anonymityFlag;
    private String areaCode;
    private String storyTitle;
    private String storyWriter;
    private String storyContents;
    private Integer readCount;
    private String fileName;
    private String orgFileName;

    @JsonFormat(pattern = "yyyy-MM-dd a h:mm:ss", locale = "ko")
    private LocalDateTime writeTime;

    @JsonFormat(pattern = "yyyy-MM-dd a h:mm:ss", locale = "ko")
    private LocalDateTime modifyTime;

    private String modifierId;
    private String delFlag;

    private String writerId;

    private Integer letterPaper;

    private Integer letterFont;

    private List<DonationStoryCommentResponseDto> comments;

    // 댓글 제외
    public static DonationStoryResponseDto fromEntity(DonationStory entity) {
        boolean isAnonymous = "Y".equals(entity.getAnonymityFlag());

        return DonationStoryResponseDto.builder()
                .storySeq(entity.getId())
                .anonymityFlag(entity.getAnonymityFlag())
                .areaCode(entity.getAreaCode())
                .storyTitle(entity.getTitle())
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
                .letterPaper(entity.getLetterPaper())
                .letterFont(entity.getLetterFont())
                .comments(null)
                .build();
    }

    //  댓글 포함
    public static DonationStoryResponseDto fromEntity(DonationStory entity, List<DonationStoryComment> commentEntities) {
        List<DonationStoryCommentResponseDto> commentDtos = commentEntities.stream()
                .map(comment -> DonationStoryCommentResponseDto.builder()
                        .id(comment.getCommentSeq())
                        .writer(comment.getWriter())
                        .contents(comment.getContents())
                        .writeTime(comment.getWriteTime())
                        .build())
                .collect(Collectors.toList());

        boolean isAnonymous = "Y".equals(entity.getAnonymityFlag());

        return DonationStoryResponseDto.builder()
                .storySeq(entity.getId())
                .anonymityFlag(entity.getAnonymityFlag())
                .areaCode(entity.getAreaCode())
                .storyTitle(entity.getTitle())
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
                .letterPaper(entity.getLetterPaper())
                .letterFont(entity.getLetterFont())
                .comments(commentDtos)
                .build();
    }

}
