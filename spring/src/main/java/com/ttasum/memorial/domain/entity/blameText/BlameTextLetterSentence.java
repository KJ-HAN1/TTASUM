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

    // BlameTextLetterSentence가 하나의 BlameTextLetter에 연결
    @ManyToOne(fetch = FetchType.LAZY)  //  지연 로딩(실제로 사용할 때 DB에서 조회)
    @JoinColumn(name = "letter_seq", insertable = false, updatable = false)  //외래 키(letter_seq)를 참조해서 연관된 BlameTextLetter를 찾음,
    private BlameTextLetter letter;
}