package com.ttasum.memorial.domain.repository.recipientLetter;

import com.ttasum.memorial.domain.entity.recipientLetter.RecipientLetterComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipientLetterCommentRepository extends JpaRepository<RecipientLetterComment, Integer> {
    Long countByLetterSeq_LetterSeqAndDelFlag(Integer letterSeq, String delFlag);
}
