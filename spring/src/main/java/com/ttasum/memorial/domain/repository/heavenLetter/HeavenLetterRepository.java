package com.ttasum.memorial.domain.repository.heavenLetter;

import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetter;
import com.ttasum.memorial.domain.entity.memorial.Memorial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HeavenLetterRepository extends JpaRepository<HeavenLetter, Integer> {
    List<HeavenLetter> findByDonateSeqAndDelFlagOrderByWriteTimeDesc(Memorial memorial, String delFlag);
}