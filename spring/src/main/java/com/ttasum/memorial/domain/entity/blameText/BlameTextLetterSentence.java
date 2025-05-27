package com.ttasum.memorial.domain.entity.blameText;

import lombok.*;

import javax.persistence.*;

@Entity
@IdClass(BlameTextLetterSentenceId.class)
@Table(name = "blame_text_letter_sentence")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Builder(toBuilder = true)
@AllArgsConstructor
public class BlameTextLetterSentence {

    @Id
    @Column(name = "letter_seq", nullable = false)
    private int letterSeq;

    @Id
    @Column(nullable = false)
    private int seq;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String sentence;

    @Column
    private Double confidence;

    @Column(nullable = false)
    private int label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "letter_seq", insertable = false, updatable = false)
    private BlameTextLetter letter;
}