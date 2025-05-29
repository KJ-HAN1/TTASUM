package com.ttasum.memorial.domain.entity.blameText;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// 복합 기본 키를 정의
@Getter
@NoArgsConstructor
@EqualsAndHashCode  //두 객체가 같은지 비교하기 위해 반드시 필요 (식별자 비교 시)
public class BlameTextLetterSentenceId implements Serializable {  //IdClass로 사용할 클래스는 반드시 직렬화 가능
    private int letterSeq;
    private int seq;
}