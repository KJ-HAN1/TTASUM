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
    // TODO: 소프트 삭제 플래그가 없으므로 화면에서 보여줄 때 조회하지 않도록 로직에서 처리하거나, DB에 컬럼을 추가하는 방법을 고려

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_seq")
    private Integer replySeq;

    @Column(name = "reply_contents", length = 3000)
    private String replyContents;

    @Column(name = "reply_write_time")
    private LocalDateTime replyWriteTime;

    // 다대일 연관 관계 설정: tb25_400_memorial.donate_seq를 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donate_seq", nullable = false)
    private Memorial memorial;

    @Builder
    private MemorialReply(String replyContents, LocalDateTime replyWriteTime, Memorial memorial) {
        this.replyContents = replyContents;
        this.replyWriteTime = replyWriteTime;
        this.memorial = memorial;
    }

    // 댓글 수정
    public void updateContents(String newContents) {
        this.replyContents = newContents;
    }
}
