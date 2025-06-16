package com.ttasum.memorial.dto.heavenLetter.response;

import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetter;
import com.ttasum.memorial.domain.entity.memorial.Memorial;
import com.ttasum.memorial.util.NameMaskUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class HeavenLetterResponseDto {
    //등록
    private boolean success;
    private int code;
    private String message;

    //등록 - 성공 201
    public static HeavenLetterResponseDto success(){

        return new HeavenLetterResponseDto(true, 201, "편지가 성공적으로 등록되었습니다.");
    }
    //등록 - 실패 400, 500
    public static HeavenLetterResponseDto fail(int code, String message){
        return new HeavenLetterResponseDto(false,code,message);
    }

    //조회 - 단건
    @Getter
    @NoArgsConstructor
    public static class HeavenLetterDetailResponse {
        private Integer letterSeq;
        private Integer donateSeq;
        private String areaCode;
        private String letterTitle;
//        private String letterPasscode;
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
        private List<HeavenLetterCommentResponseDto.CommentListResponse> comments;

        public HeavenLetterDetailResponse(HeavenLetter heavenLetter) {
            this.letterSeq = heavenLetter.getLetterSeq();
//            this.donateSeq = heavenLetter.getDonateSeq().getDonateSeq() != null ? heavenLetter.getDonateSeq().getDonateSeq() : null;
            Memorial memorial = heavenLetter.getDonateSeq();
            this.donateSeq = (memorial != null && memorial.getDonateSeq() != null)
                    ? memorial.getDonateSeq()
                    : null;
            this.areaCode = heavenLetter.getAreaCode();
            this.letterTitle = heavenLetter.getLetterTitle();
//            this.letterPasscode = heavenLetter.getLetterPasscode();
            this.donorName = NameMaskUtil.maskDonorNameIfAnonymous(
                    heavenLetter.getDonorName(),
                    heavenLetter.getAnonymityFlag()
            );
            this.letterWriter =  NameMaskUtil.maskRecipientNameIfAnonymous(
                    heavenLetter.getLetterWriter(),
                    heavenLetter.getAnonymityFlag()
            );
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
            this.comments = heavenLetter.getComments().stream()
                    .map(HeavenLetterCommentResponseDto.CommentListResponse::new)
                    .collect(Collectors.toList());
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

        public HeavenLetterListResponse(HeavenLetter heavenLetter) {
            this.letterSeq = heavenLetter.getLetterSeq();
//            this.donateSeq = heavenLetter.getDonateSeq().getDonateSeq() != null ? heavenLetter.getDonateSeq().getDonateSeq() : null;
            Memorial memorial = heavenLetter.getDonateSeq();
            this.donateSeq = (memorial != null && memorial.getDonateSeq() != null)
                    ? memorial.getDonateSeq()
                    : null;
            this.letterTitle = heavenLetter.getLetterTitle();
            this.donorName = NameMaskUtil.maskDonorNameIfAnonymous(
                    heavenLetter.getDonorName(),
                    heavenLetter.getAnonymityFlag()
            );
            this.letterWriter =  NameMaskUtil.maskRecipientNameIfAnonymous(
                    heavenLetter.getLetterWriter(),
                    heavenLetter.getAnonymityFlag()
            );
            this.writeTime = heavenLetter.getWriteTime();
            this.readCount = heavenLetter.getReadCount();
        }
        public static HeavenLetterListResponse fromEntity(HeavenLetter heavenLetterEntity) {
            return new HeavenLetterListResponse(heavenLetterEntity);
        }
    }

}





