package com.ttasum.memorial.domain.entity.blameText;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blame_text_letter")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class BlameTextLetter {

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

    @OneToMany(mappedBy = "letter")
    private List<BlameTextLetterSentence> sentences = new ArrayList<>();
}
