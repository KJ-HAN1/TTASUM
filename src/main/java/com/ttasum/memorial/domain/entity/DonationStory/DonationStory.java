package com.ttasum.memorial.domain.entity.DonationStory;

import com.ttasum.memorial.dto.DonationStory.DonationStoryUpdateRequestDto;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb25_420_donation_story")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DonationStory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_seq")
    private Integer id;

    @Column(name = "area_code", length = 10)
    private String areaCode;

    @Column(name = "story_title", length = 600)
    private String title;

    @Column(name = "donor_name", length = 150)
    private String donorName;

    @Column(name = "story_passcode", length = 60)
    private String passcode;

    @Column(name = "story_writer", length = 150)
    private String writer;

    @Column(name = "anonymity_flag", length = 1)
    private String anonymityFlag;

    @Column(name = "read_count")
    private Integer readCount;

    @Lob // 긴 문자열 데이터를 저장
    @Column(name = "story_contents", columnDefinition = "TEXT")
    private String contents;

    @Column(name = "file_name", length = 600)
    private String fileName;

    @Column(name = "org_file_name", length = 600)
    private String originalFileName;

    @CreationTimestamp
    @Column(name = "write_time", nullable = false, updatable = false)
    private LocalDateTime writeTime;

    @Column(name = "writer_id", length = 60)
    private String writerId;

    @UpdateTimestamp
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;

    @Column(name = "modifier_id", length = 60)
    private String modifierId;

    @Column(name = "del_flag", length = 1,
            columnDefinition = "varchar(1) default 'N'", nullable = false)
    private String delFlag = "N";

    @Builder
    public DonationStory(String areaCode, String title, String donorName,
                         String passcode, String writer, String anonymityFlag,
                         Integer readCount, String contents, String fileName,
                         String originalFileName, String writerId, String modifierId) {
        this.areaCode = areaCode;
        this.title = title;
        this.donorName = donorName;
        this.passcode = passcode;
        this.writer = writer;
        this.anonymityFlag = anonymityFlag;
        this.readCount = readCount != null ? readCount : 0;
        this.contents = contents;
        this.fileName = fileName;
        this.originalFileName = originalFileName;
        this.writerId = writerId;
        this.modifierId = modifierId;
        this.delFlag = "N";
    }

    public void increaseReadCount() {
        this.readCount = (this.readCount == null ? 1 : this.readCount + 1);
    }

    public void update(DonationStoryUpdateRequestDto dto) {
        this.title = dto.getTitle();
        this.donorName = dto.getDonorName();
        this.writer = dto.getWriter();
        this.areaCode = dto.getAreaCode();
        this.contents = dto.getContents();
        this.fileName = dto.getFileName();
        this.originalFileName = dto.getOriginalFileName();
        this.modifierId = dto.getModifierId();
        this.passcode = dto.getPasscode();
    }

    public void delete(String modifierId) {
        this.delFlag = "Y";
        this.modifierId = modifierId;
        this.modifyTime = LocalDateTime.now();
    }

    // 테스트용으로만 열어두는 패키지 가시성 세터
    void setStorySeq(Integer storySeq) {
        this.id = storySeq;
    }
}
