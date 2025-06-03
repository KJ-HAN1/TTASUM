package com.ttasum.memorial.domain.entity.blameText;

import com.ttasum.memorial.domain.entity.Contents;
import com.ttasum.memorial.domain.entity.DonationStory.DonationStoryComment;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)  //JPA는 접근 가능하지만, 외부에서 생성 제한 가능
@AllArgsConstructor
@Entity
@Table(name = "blame_text_comment")
public class BlameTextComment extends Contents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false)
    private Integer seq;

    @Size(max = 20)
    @NotNull
    @Column(name = "board_type", nullable = false, length = 20)
    private String boardType;

    @Column(name = "confidence", nullable = false)
    private Double blameConfidence;

    @NotNull
    @Column(name = "regulation", nullable = false)
    private Double regulation;

    @NotNull
    @Column(name = "sentence_line", nullable = false)
    private Integer sentenceLine;

    @NotNull
    @Column(name = "story_seq", nullable = false)
    private Integer storySeq;

    @NotNull
    @Lob
    @Column(name = "contents", nullable = false)
    private String contents;

    @NotNull
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    @NotNull
    @Column(name = "delete_flag", nullable = false)
    private Integer deleteFlag;

    @NotNull
    @Column(name = "blame_label", nullable = false)
    private Integer blameLabel;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_seq", referencedColumnName = "comment_seq")
    private DonationStoryComment comment;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<BlameTextCommentSentence> comments = new ArrayList<>();

}