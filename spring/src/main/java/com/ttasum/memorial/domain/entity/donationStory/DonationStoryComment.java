package com.ttasum.memorial.domain.entity.donationStory;

import com.ttasum.memorial.domain.entity.Comment;
import com.ttasum.memorial.exception.donationStory.InvalidCommentPasscodeException;
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
public class DonationStoryComment extends Comment {

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
     * 비밀번호가 일치하는 경우 댓글 내용을 수정
     * @param inputPasscode 요청자가 입력한 비밀번호
     * @param newContents 수정할 내용
     * @param modifierId 수정자 ID (로그인 사용자 또는 null)
     */
    public DonationStoryComment updateIfPasscodeMatches(String inputPasscode, String newContents, String modifierId) {
        if (!this.passcode.equals(inputPasscode)) {
            throw new InvalidCommentPasscodeException(this.commentSeq);
        }
        this.contents = newContents;
        this.modifierId = modifierId;
        this.modifyTime = LocalDateTime.now();
        return this;
    }

    // 비밀번호가 일치하는 경우 댓글 삭제(소프트 삭제)
    public DonationStoryComment deleteIfPasscodeMatches(String inputPasscode, String modifierId) {
        if (!this.passcode.equals(inputPasscode)) {
            throw new InvalidCommentPasscodeException(this.commentSeq);
        }
        this.delFlag = "Y";
        this.modifierId = modifierId;
        this.modifyTime = LocalDateTime.now();
        return this;
    }
}
