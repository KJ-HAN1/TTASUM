package com.ttasum.memorial.domain.repository.Main;

import com.ttasum.memorial.domain.entity.Main.HeavenLetterMainEntity;
import com.ttasum.memorial.dto.Main.HeavenLetterMainDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HeavenLetterMainRepository extends JpaRepository<HeavenLetterMainEntity, Integer> {
    @Query("SELECT new com.ttasum.memorial.dto.Main.HeavenLetterMainDto(h.letterSeq, h.letterTitle, h.donorName, h.letterWriter) " +
            "FROM HeavenLetterMainEntity h " +
            "WHERE h.delFlag = 'N' " +
            "ORDER BY h.writeTime DESC")
    List<HeavenLetterMainDto> findRecentHeavenLetters(Pageable pageable);
}
