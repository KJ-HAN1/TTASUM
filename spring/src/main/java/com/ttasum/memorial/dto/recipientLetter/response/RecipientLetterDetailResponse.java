package com.ttasum.memorial.dto.recipientLetter.response;

import com.ttasum.memorial.domain.entity.recipientLetter.RecipientLetter;
import com.ttasum.memorial.dto.recipientLetterComment.response.RecipientLetterCommentListResponseDto;
//import com.ttasum.memorial.util.OrganCodeUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

//조회 단건
@Getter
@Setter
@NoArgsConstructor

public class RecipientLetterDetailResponse {
    private Integer letterSeq;
    private String organCode;
    private String organEtc;
    private String storyTitle;
    private String recipientYear;
    private String letterPasscode;
    private String letterWriter;
    private String anonymityFlag;
    private Integer readCount;
    private String letterContents;
    private String fileName;
    private String orgFileName;
    private LocalDateTime writeTime;
    private String writerId;
    private LocalDateTime modifyTime;
    private String modifierId;
    private String delFlag;
//    private String organName;
    private List<RecipientLetterCommentListResponseDto> comments;

    public RecipientLetterDetailResponse(RecipientLetter recipientLetter) {
        this.letterSeq = recipientLetter.getLetterSeq();
        this.organCode = recipientLetter.getOrganCode();
        this.organEtc = recipientLetter.getOrganEtc();
        this.storyTitle = recipientLetter.getStoryTitle();
        this.recipientYear = recipientLetter.getRecipientYear();
        this.letterPasscode = recipientLetter.getLetterPasscode();
        this.letterWriter = recipientLetter.getLetterWriter();
        this.anonymityFlag = recipientLetter.getAnonymityFlag();
        this.readCount = recipientLetter.getReadCount();
        this.letterContents = recipientLetter.getLetterContents();
        this.fileName = recipientLetter.getFileName();
        this.orgFileName = recipientLetter.getOrgFileName();
        this.writeTime = recipientLetter.getWriteTime();
        this.writerId = recipientLetter.getWriterId();
        this.modifyTime = recipientLetter.getModifyTime();
        this.modifierId = recipientLetter.getModifierId();
        this.delFlag = recipientLetter.getDelFlag();
//        this.organName = resolveOrganName(recipientLetter);
        this.comments =  recipientLetter.getComments().stream()
                .map(RecipientLetterCommentListResponseDto::new)
                .collect(Collectors.toList());
    }
//    private String resolveOrganName(RecipientLetter recipientLetter) {
//        return "ORGAN000".equals(recipientLetter.getOrganCode())
//                ? recipientLetter.getOrganEtc()
//                : OrganCodeUtil.resolveNameByCode(recipientLetter.getOrganCode());
//    }

}


