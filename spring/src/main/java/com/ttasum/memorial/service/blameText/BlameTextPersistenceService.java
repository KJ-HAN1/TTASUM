package com.ttasum.memorial.service.blameText;

import com.ttasum.memorial.domain.entity.DonationStory.DonationStory;
import com.ttasum.memorial.domain.entity.Story;
import com.ttasum.memorial.domain.entity.blameText.BlameTextLetter;
import com.ttasum.memorial.domain.entity.blameText.BlameTextLetterSentence;
import com.ttasum.memorial.domain.repository.blameText.BlameTextLetterRepository;
import com.ttasum.memorial.domain.repository.blameText.BlameTextLetterSentenceRepository;
import com.ttasum.memorial.dto.blameText.BlameResponseDto;
import com.ttasum.memorial.exception.blameText.BlamTextException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.TransientDataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class BlameTextPersistenceService {
    private final BlameTextLetterRepository blameTextLetterRepository;
    private final BlameTextLetterSentenceRepository blameTextLetterSentenceRepository;

    // 매직 넘버 제거
    private static final double DEFAULT_REGULATION = 0.7;
    private static final String DEFAULT_BOARD_TYPE = "donation";


    // 새로운 트랜잭션: 감정 분석과 DB 저장이 별개로 rollback 되어야 할 때
    @Transactional(propagation = Propagation.REQUIRES_NEW)  //호출자가 트랜잭션 중이더라도 기존 트랜잭션을 분리
    public void saveToDb(BlameResponseDto response, Story story) {
        BlameTextLetter savedLetter = blameTextLetterRepository.save(
                Objects.requireNonNull(setBlameLetter(response, story)));
        ArrayList<BlameTextLetterSentence> list = setBlameLetterSentence(response, savedLetter);
        blameTextLetterSentenceRepository.saveAll(list);
    }

    // python 서버에서 받은 json 응답 값을 엔티티에 매핑
    private BlameTextLetter setBlameLetter(BlameResponseDto response, Story story) {
        try {
            // Donation 게시글인 경우
            if(story instanceof DonationStory) {
                return BlameTextLetter.builder()
                        .sentence(response.getSentence())
                        .blameLabel(response.getLabel())
                        .blameConfidence(response.getConfidence())
                        .sentenceLine(response.getDetails().size())
                        .regulation(DEFAULT_REGULATION)  //임의 설정
                        .updateTime(LocalDateTime.now())
                        .boardType(DEFAULT_BOARD_TYPE)  //임의 설정
                        .donationStory((DonationStory) story)
                        .build();
            }
            return null;
        } catch (NullPointerException e) {
            throw new BlamTextException("Null pointer exception.");
        }
    }

    // python 서버 json 응답 값 중 Text를 sentence로 split한 결과를 DB에 저장
    private ArrayList<BlameTextLetterSentence> setBlameLetterSentence(BlameResponseDto response, BlameTextLetter savedLetter){
        int seq = 0;
        ArrayList<BlameTextLetterSentence> list = new ArrayList<>();
        for (Map<String, Object> oneDetail : response.getDetails() ) {
            BlameTextLetterSentence blameTextLetterSentence = BlameTextLetterSentence.builder()
                    .letterSeq(savedLetter.getSeq())
                    .seq(seq++)
                    .sentence((String) oneDetail.get("sentence"))
                    .confidence((double) oneDetail.get("confidence"))
                    .label((int) oneDetail.get("label")).build();
            list.add(blameTextLetterSentence);
        }
        return list;
    }
}
