package com.ttasum.memorial.domain.repository.recipientLetter;

import com.ttasum.memorial.domain.entity.recipientLetter.RecipientLetter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * 수혜자 편지 조회용 Repository
 * - @Where 전역 필터로 delFlag='N' 자동 적용
 */
public interface RecipientLetterRepository extends JpaRepository<RecipientLetter, Integer>, JpaSpecificationExecutor<RecipientLetter> {

    /**
     * 전체 목록 페이징 조회 (delFlag='N')
     */
    Page<RecipientLetter> findAll(Pageable pageable);

    /**
     * 단건 조회 (댓글 포함, delFlag='N')
     */
    @EntityGraph(attributePaths = "comments")
    Optional<RecipientLetter> findWithCommentsByLetterSeq(Integer letterSeq);
}




