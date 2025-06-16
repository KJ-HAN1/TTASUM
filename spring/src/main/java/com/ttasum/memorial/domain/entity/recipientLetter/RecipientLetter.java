package com.ttasum.memorial.domain.entity.recipientLetter;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "tb25_430_recipient_letter")

public class RecipientLetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "letter_seq", nullable = false)
    private Integer letterSeq;

    @Column(name = "organ_code", length = 10)
    private String organCode;

    @Column(name = "organ_etc", length = 60)
    private String organEtc;

    @Column(name = "story_title", length = 600)
    private String storyTitle;

    @Column(name = "recipient_year", length = 10)
    private String recipientYear;

    @Column(name = "letter_passcode", length = 60)
    private String letterPasscode;

    @Column(name = "letter_writer", length = 150)
    private String letterWriter;

    @Column(name = "anonymity_flag", length = 1)
    private String anonymityFlag;

    @Column(name = "read_count")
    private Integer readCount;

    @Lob
    @Column(name = "letter_contents" , columnDefinition = "TEXT")
    private String letterContents;

    @Column(name = "file_name", length = 600)
    private String fileName;

    @Column(name = "org_file_name", length = 600)
    private String orgFileName;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "write_time", nullable = false)
    private LocalDateTime writeTime;

    @Column(name = "writer_id", length = 60)
    private String writerId;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "modify_time")
    private LocalDateTime modifyTime;

    @Column(name = "modifier_id", length = 60)
    private String modifierId;

    @ColumnDefault("'N'")
    @Column(name = "del_flag", nullable = false, length = 1)
    private String delFlag;

    //JPA생명주기 중 하나 : save()로 DB에 insert되기 직전에 실행(기본값 세팅)
    //update할 땐 작동 안 함
    @PrePersist
    public void prePersist() {
        //편지 작성 시간 — 저장 전에 현재 시간으로 자동 설정
        this.writeTime = LocalDateTime.now();
        //삭제 여부 — 기본값 'N'으로 설정 (삭제 안 됨)
        this.delFlag = "N";
        //조회수 — 처음엔 0으로 초기화
        this.readCount = 0;
    }
}