package com.ttasum.memorial.domain.repository.heavenLetter;

import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetter;
import com.ttasum.memorial.domain.entity.heavenLetter.Memorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemorialRepository extends JpaRepository<Memorial, Integer> {
}
