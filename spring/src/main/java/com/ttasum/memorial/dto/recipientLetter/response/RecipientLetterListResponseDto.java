package com.ttasum.memorial.dto.recipientLetter.response;

import com.ttasum.memorial.domain.entity.recipientLetter.RecipientLetter;
import com.ttasum.memorial.util.NameMaskUtil;
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

    public RecipientLetterListResponseDto (RecipientLetter recipientLetter) {
        this.letterSeq = recipientLetter.getLetterSeq();
        this.storyTitle = recipientLetter.getStoryTitle();
        this.letterWriter = NameMaskUtil.maskRecipientNameIfAnonymous(
                recipientLetter.getLetterWriter(),
                recipientLetter.getAnonymityFlag()
        );
        this.writeTime = recipientLetter.getWriteTime();
        this.readCount = recipientLetter.getReadCount();
    }
    public static RecipientLetterListResponseDto fromEntity(RecipientLetter recipientLetterEntity) {
        return new RecipientLetterListResponseDto(recipientLetterEntity);
    }
}

