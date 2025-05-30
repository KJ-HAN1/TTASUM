package com.ttasum.memorial.domain.entity.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb25_220_article_file_dtl")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeFile {

    @EmbeddedId
    private NoticeFileId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "board_code", referencedColumnName = "board_code", insertable = false, updatable = false),
            @JoinColumn(name = "article_seq", referencedColumnName = "article_seq", insertable = false, updatable = false)
    }) // 중복 매핑 방지를 위해 읽기 전용으로 설정
    private Notice notice;

    @Column(name = "file_path_name", length = 600)
    private String filePathName;

    @Column(name = "file_name", length = 600)
    private String fileName;

    @Column(name = "org_file_name", length = 600)
    private String orgFileName;

    @Column(name = "del_flag", length = 1, nullable = false)
    private String delFlag;

    @Builder.Default
    @Column(name = "write_time", nullable = false, updatable = false)
    private LocalDateTime writeTime = LocalDateTime.now();

    @Column(name = "writer_id", length = 60, nullable = false)
    private String writerId;

    @Builder.Default
    @Column(name = "modify_time", nullable = false)
    private LocalDateTime modifyTime = LocalDateTime.now();

    @Column(name = "modifier_id", length = 60, nullable = false)
    private String modifierId;
}
