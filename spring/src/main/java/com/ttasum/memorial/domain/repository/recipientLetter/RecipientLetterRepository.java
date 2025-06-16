package com.ttasum.memorial.domain.repository.recipientLetter;

import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetter;
import com.ttasum.memorial.domain.entity.recipientLetter.RecipientLetter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RecipientLetterRepository extends JpaRepository<RecipientLetter, Integer>, JpaSpecificationExecutor<RecipientLetter> {


    //기본 전체 조회("N"만 조회), 10개 페이징처리
    Page<RecipientLetter> findAllByDelFlag(String deFlag, Pageable pageable);

}
