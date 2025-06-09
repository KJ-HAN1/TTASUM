package com.ttasum.memorial.domain.repository.memorial;

import com.ttasum.memorial.domain.entity.memorial.Memorial;
import com.ttasum.memorial.dto.memorial.response.MemorialResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// 기본 CRUD 기능 제공 JPA 리포지터리
public interface MemorialRepository extends JpaRepository<Memorial, Integer> {

    @Query("""
        SELECT new com.ttasum.memorial.dto.memorial.response.MemorialResponseDto(
            m.donateSeq,
            m.donorName,
            m.genderFlag,
            m.donateAge,
            m.donateDate,
            COUNT(r.replySeq),
            m.donorBirthdate
        )
        FROM Memorial m
        LEFT JOIN m.replies r
        WHERE m.delFlag = 'N'
          AND (:donorName IS NULL OR :donorName = '' OR m.donorName LIKE %:donorName%)
          AND (:startDate IS NULL OR :endDate IS NULL 
               OR (m.donateDate >= :startDate AND m.donateDate <= :endDate))
        GROUP BY m.donateSeq, m.donorName, m.genderFlag, m.donateAge, m.donateDate, m.donorBirthdate
        """)
    Page<MemorialResponseDto> findByFilter(
            @Param("donorName") String donorName,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            Pageable pageable);

    Optional<Memorial> findByDonateSeqAndDelFlag(Integer donateSeq, String delFlag);
}
