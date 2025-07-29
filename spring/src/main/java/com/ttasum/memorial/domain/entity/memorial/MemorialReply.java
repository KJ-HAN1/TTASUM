package com.ttasum.memorial.domain.entity.memorial;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb25_401_memorial_reply")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemorialReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_seq")
    private Integer replySeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donate_seq", nullable = false)
    private Memorial memorial;

    @Column(name = "reply_writer", length = 150)
    private String replyWriter;           // 댓글 작성자 이름

    @Column(name = "reply_password", length = 60)
    private String replyPassword;         // 댓글 비밀번호

    @Column(name = "reply_writer_id", length = 60)
    private String replyWriterId;         // 로그인 사용자 ID (로그인 댓글일 때)

    @Column(name = "reply_contents", length = 3000)
    private String replyContents;         // 댓글 내용

    @Column(name = "reply_write_time")
    private LocalDateTime replyWriteTime; // 댓글 등록일시

    @Column(name = "reply_modify_time")
    private LocalDateTime replyModifyTime; // 댓글 수정일시

    @Column(name = "reply_modifier_id", length = 60)
    private String replyModifierId;       // 마지막 수정자 ID

    @Column(name = "del_flag", length = 1)
    private String delFlag;               // 소프트 삭제 플래그 (Y/N)

    @Builder
    private MemorialReply(String replyWriter,
                          String replyPassword,
                          String replyWriterId,
                          String replyContents,
                          LocalDateTime replyWriteTime,
                          Memorial memorial,
                          String delFlag) {
        this.replyWriter      = replyWriter;
        this.replyPassword    = replyPassword;
        this.replyWriterId    = replyWriterId;
        this.replyContents    = replyContents;
        this.replyWriteTime   = replyWriteTime;
        this.memorial         = memorial;
        this.delFlag          = delFlag;
    }

    /** 댓글 수정 **/
    public void updateComment(String newContents, String modifierId) {
        this.replyContents     = newContents;
        this.replyModifierId   = modifierId;
        this.replyModifyTime   = LocalDateTime.now();
    }

    /** 댓글 삭제(소프트) **/
    public void deleteComment(String modifierId) {
        this.delFlag           = "Y";
        this.replyModifierId   = modifierId;
        this.replyModifyTime   = LocalDateTime.now();
    }
}
