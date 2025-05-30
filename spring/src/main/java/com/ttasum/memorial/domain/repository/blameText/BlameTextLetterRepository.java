package com.ttasum.memorial.domain.repository.blameText;

import com.ttasum.memorial.domain.entity.Story;
import com.ttasum.memorial.domain.entity.blameText.BlameTextLetter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlameTextLetterRepository extends JpaRepository<BlameTextLetter, Integer> {
    Optional<BlameTextLetter> findByDonationStory_IdAndDeleteFlag(Integer originSeq, int deleteFlag);
}
