package com.ttasum.memorial.dto.heavenLetter.response;

import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class HeavenLetterResponse {
    //등록
    private boolean success;
    private int code;
    private String message;

    //등록 - 성공 201
    public static HeavenLetterResponse success(){

        return new HeavenLetterResponse(true, 201, "편지가 성공적으로 등록되었습니다.");
    }
    //등록 - 실패 400, 500
    public static HeavenLetterResponse fail(int code, String message){
        return new HeavenLetterResponse(false,code,message);
    }

    //조회 - 단건
    @Getter
    @NoArgsConstructor
    public static class HeavenLetterDetailResponse {
        private Integer letterSeq;
        private Integer donateSeq;
        private String areaCode;
        private String letterTitle;
        private String letterPasscode;
        private String donorName;
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

        public HeavenLetterDetailResponse(HeavenLetter heavenLetter) {
            this.letterSeq = heavenLetter.getLetterSeq();
            this.donateSeq = heavenLetter.getDonateSeq().getDonateSeq();
            this.areaCode = heavenLetter.getAreaCode();
            this.letterTitle = heavenLetter.getLetterTitle();
            this.letterPasscode = heavenLetter.getLetterPasscode();
            this.donorName = heavenLetter.getDonorName();
            this.letterWriter = heavenLetter.getLetterWriter();
            this.anonymityFlag = heavenLetter.getAnonymityFlag();
            this.readCount = heavenLetter.getReadCount();
            this.letterContents = heavenLetter.getLetterContents();
            this.fileName = heavenLetter.getFileName();
            this.orgFileName = heavenLetter.getOrgFileName();
            this.writeTime = heavenLetter.getWriteTime();
            this.writerId = heavenLetter.getWriterId();
            this.modifyTime = heavenLetter.getModifyTime();
            this.modifierId = heavenLetter.getModifierId();
            this.delFlag = heavenLetter.getDelFlag();
        }
    }
    //조회 - 목록
    @Getter
    @NoArgsConstructor
    public static class HeavenLetterListResponse {
        private Integer letterSeq;
        private Integer donateSeq;
        private String letterTitle;
        private String donorName;
        private String letterWriter;
        private LocalDateTime writeTime;
        private Integer readCount;

        public HeavenLetterListResponse(HeavenLetter heavenletter) {
            this.letterSeq = heavenletter.getLetterSeq();
            this.donateSeq = heavenletter.getDonateSeq().getDonateSeq();
            this.letterTitle = heavenletter.getLetterTitle();
            this.donorName = heavenletter.getDonorName();
            this.letterWriter = heavenletter.getLetterWriter();
            this.writeTime = heavenletter.getWriteTime();
            this.readCount = heavenletter.getReadCount();
        }
    }

}





