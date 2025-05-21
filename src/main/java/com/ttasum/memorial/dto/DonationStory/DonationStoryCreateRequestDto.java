package com.ttasum.memorial.dto.DonationStory;

import com.ttasum.memorial.domain.entity.DonationStory.DonationStory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 기증후 스토리 등록 요청 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class DonationStoryCreateRequestDto {

    @NotBlank
    @Size(max = 10)
    private String areaCode;

    @NotBlank
    @Size(max = 600)
    private String title;

    @Size(max = 150)
    private String donorName;

    @NotBlank @Size(min = 4, max = 60, message = "passcode는 최소4글자, 최대 60글자까지 가능합니다.")
    private String passcode;

    @NotBlank
    @Size(max = 150)
    private String writer;

    @NotBlank
    @Size(max = 1)
    private String anonymityFlag;

    /**
     * 본문 내용 (story_contents)
     * (TEXT)
     */
    private String contents;

    @Size(max = 600)
    private String fileName;

    @Size(max = 600)
    private String originalFileName;

    @NotBlank
    @Size(max = 60)
    private String writerId;

    // DTO → Entity 변환
    public DonationStory toEntity() {
        return DonationStory.builder()
                .areaCode(this.areaCode)
                .title(this.title)
                .donorName(this.donorName)
                .passcode(this.passcode)
                .writer(this.writer)
                .anonymityFlag(this.anonymityFlag)
                .contents(this.contents)
                .fileName(this.fileName)
                .originalFileName(this.originalFileName)
                .writerId(this.writerId)
                .build();
    }
}
