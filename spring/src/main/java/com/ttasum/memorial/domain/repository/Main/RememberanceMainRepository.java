package com.ttasum.memorial.domain.repository.Main;

import com.ttasum.memorial.domain.entity.Main.RememberanceMainEntity;
import com.ttasum.memorial.dto.Main.RememberanceMainDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RememberanceMainRepository extends JpaRepository<RememberanceMainEntity, Integer> {
    @Query("SELECT new com.ttasum.memorial.dto.Main.RememberanceMainDto(r.donorName, r.genderFlag, r.donateAge) " +
            "FROM RememberanceMainEntity r " +
            "WHERE r.delFlag = 'N' " +
            "ORDER BY r.modifyTime DESC")
    List<RememberanceMainDto> findRecentRememberance(Pageable pageable);
}
