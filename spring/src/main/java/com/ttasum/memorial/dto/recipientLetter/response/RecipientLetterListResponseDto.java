package com.ttasum.memorial.dto.recipientLetter.response;

import com.ttasum.memorial.domain.entity.recipientLetter.RecipientLetter;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor

public class RecipientLetterListResponseDto {
    //조회 - 목록

    private Integer letterSeq;
    private String storyTitle;
    private String letterWriter;
    private LocalDateTime writeTime;
    private Integer readCount;

    public RecipientLetterListResponseDto (RecipientLetter recipientletter) {
        this.letterSeq = recipientletter.getLetterSeq();
        this.storyTitle = recipientletter.getStoryTitle();
        this.letterWriter = recipientletter.getLetterWriter();
        this.writeTime = recipientletter.getWriteTime();
        this.readCount = recipientletter.getReadCount();
    }
    public static RecipientLetterListResponseDto fromEntity(RecipientLetter recipientLetterEntity) {
        return new RecipientLetterListResponseDto(recipientLetterEntity);
    }
}

