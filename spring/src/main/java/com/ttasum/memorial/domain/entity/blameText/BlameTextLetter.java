package com.ttasum.memorial.domain.entity.blameText;

import com.ttasum.memorial.domain.entity.Contents;
import com.ttasum.memorial.domain.entity.DonationStory.DonationStory;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blame_text_letter")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BlameTextLetter extends Contents {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String sentence;

    @Column(name = "blame_label", nullable = false)
    private int blameLabel;

    @Column(name = "blame_confidence", nullable = false)
    private double blameConfidence;

    @Column(name = "sentence_line", nullable = false)
    private int sentenceLine;

    @Column(nullable = false)
    private double regulation;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Column(name = "board_type", nullable = false, length = 20)
    private String boardType;

    @OneToMany(mappedBy = "letter", cascade = CascadeType.PERSIST)
    @Builder.Default
    private List<BlameTextLetterSentence> sentences = new ArrayList<>();

    @Column(name = "delete_flag", nullable = false)
    private int deleteFlag;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="origin_seq", referencedColumnName = "story_seq")
    private DonationStory donationStory;
}
