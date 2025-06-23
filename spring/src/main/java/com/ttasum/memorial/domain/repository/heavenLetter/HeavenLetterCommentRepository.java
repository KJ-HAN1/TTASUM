package com.ttasum.memorial.domain.repository.heavenLetter;

import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetterComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HeavenLetterCommentRepository extends JpaRepository<HeavenLetterComment, Integer> {
    Long countByLetterSeq_LetterSeqAndDelFlag(Integer letterSeq, String delFlag);
}

