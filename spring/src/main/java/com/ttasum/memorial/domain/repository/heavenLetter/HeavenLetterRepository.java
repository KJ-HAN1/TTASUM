package com.ttasum.memorial.domain.repository.heavenLetter;

import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface HeavenLetterRepository extends JpaRepository<HeavenLetter, Integer> , JpaSpecificationExecutor<HeavenLetter> {

    //기본 전체 조회("N"만 조회), 10개 페이징처리
    Page<HeavenLetter> findAllByDelFlag(String deFlag, Pageable pageable);

    //단건 조회(댓글 포함)
    @EntityGraph(attributePaths = "comments")
    Optional<HeavenLetter> findByLetterSeqAndDelFlag(Integer letterSeq, String delFlag);

}
