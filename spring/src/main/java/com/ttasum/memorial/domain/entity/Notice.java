package com.ttasum.memorial.domain.entity;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb25_210_article_dtl")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notice {

    @EmbeddedId
    private ArticleId articleId;

    @Column(length = 600, nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String contents;

    @Column(name = "read_count")
    private Integer readCount;

    @Column(name = "reply_title", length = 600)
    private String replyTitle;

    @Column(name = "reply_contents", length = 3000)
    private String replyContents;

    @Column(name = "reply_writer_id", length = 60)
    private String replyWriterId;

    @Column(name = "fix_flag", length = 1)
    private String fixFlag;

    @Column(name = "article_passcode", length = 60)
    private String articlePasscode;

    @Column(name = "article_url", length = 600)
    private String articleUrl;

    @Column(name = "del_flag", length = 1, nullable = false)
    private String delFlag;

    @Column(name = "write_time", nullable = false)
    private LocalDateTime writeTime;

    @Column(name = "writer_id", length = 60, nullable = false)
    private String writerId;

    @Column(name = "modify_time", nullable = false)
    private LocalDateTime modifyTime;

    @Column(name = "modifier_id", length = 60, nullable = false)
    private String modifierId;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    public void increaseReadCount() {
        if (this.readCount == null) {
            this.readCount = 1;
        }else {
            this.readCount++;
        }
    }
}
