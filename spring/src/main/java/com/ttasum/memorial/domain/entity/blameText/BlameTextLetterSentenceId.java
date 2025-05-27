package com.ttasum.memorial.domain.entity.blameText;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class BlameTextLetterSentenceId implements Serializable {
    private int letterSeq;
    private int seq;
}