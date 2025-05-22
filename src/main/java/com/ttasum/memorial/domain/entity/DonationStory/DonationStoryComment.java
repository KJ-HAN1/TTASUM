package com.ttasum.memorial.domain.entity.DonationStory;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

// 기증후 스토리 댓글 엔티티
@Entity
@Table(name = "tb25_421_donation_story_comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 접근, 외부 생성 제한
public class DonationStoryComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_seq")
    private Integer commentSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "story_seq")
    private DonationStory story;

    @Column(name = "comment_writer", length = 150)
    private String writer;

    @Column(name = "comment_passcode", length = 60)
    private String passcode;

    @Lob // 긴 문자열 데이터를 저장
    @Column(name = "contents", columnDefinition = "TEXT")
    private String contents;

    @Column(name = "write_time")
    private LocalDateTime writeTime;

    @Column(name = "writer_id", length = 60)
    private String writerId;

    @Column(name = "modify_time")
    private LocalDateTime modifyTime;

    @Column(name = "modifier_id", length = 60)
    private String modifierId;

    @Column(name = "del_flag", length = 1, nullable = false)
    private String delFlag = "N";

    @Builder
    public DonationStoryComment(DonationStory story, String writer, String passcode, String contents,
                                String writerId, String modifierId) {
        this.story = story;
        this.writer = writer;
        this.passcode = passcode;
        this.contents = contents;
        this.writerId = writerId;
        this.modifierId = modifierId;
        this.writeTime = LocalDateTime.now();
        this.modifyTime = LocalDateTime.now();
        this.delFlag = "N";
    }

    /**
     * 댓글 내용 수정
     * @param contents 댓글 내용
     * @param modifierId 작성자 인증 후 사용
     */
    public void update(String contents, String modifierId) {
        this.contents = contents;
        this.modifierId = modifierId;
        this.modifyTime = LocalDateTime.now();
    }

    // 댓글 삭제(소프트 삭제)
    public void delete(String modifierId) {
        this.delFlag = "Y";
        this.modifierId = modifierId;
        this.modifyTime = LocalDateTime.now();
    }
}
