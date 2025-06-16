package com.ttasum.memorial.domain.repository.blameText;

import com.ttasum.memorial.domain.entity.Story;
import com.ttasum.memorial.domain.entity.blameText.BlameTextLetter;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlameTextLetterRepository extends JpaRepository<BlameTextLetter, Integer> {
    Optional<BlameTextLetter> findByDonationStory_IdAndDeleteFlag(Integer originSeq, int deleteFlag);
    /*
    조건
    1. 페이징(10건씩)
    2. blame_text_letter의 전체 결과와 원래 테이블(tb25_420_donation_story)의
                        (story_title, area_code, donor_name, story_writer, anonymity_flag, read_count)를
                        추가적으로 한꺼번에 조회
    */
}
