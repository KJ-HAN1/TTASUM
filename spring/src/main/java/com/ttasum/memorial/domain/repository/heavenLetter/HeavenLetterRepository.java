package com.ttasum.memorial.domain.repository.heavenLetter;

import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface HeavenLetterRepository extends JpaRepository<HeavenLetter, Integer> , JpaSpecificationExecutor<HeavenLetter> {

    //기본 전체 조회("N"만 조회), 10개 페이징처리
    Page<HeavenLetter> findAllByDelFlag(String deFlag, Pageable pageable);
}
