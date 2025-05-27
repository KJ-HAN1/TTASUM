package com.ttasum.memorial.service.blameText;

import com.ttasum.memorial.domain.entity.blameText.BlameTextLetter;
import com.ttasum.memorial.domain.entity.blameText.BlameTextLetterSentence;
import com.ttasum.memorial.domain.repository.blameText.BlameTextLetterRepository;
import com.ttasum.memorial.domain.repository.blameText.BlameTextLetterSentenceRepository;
import com.ttasum.memorial.dto.blameText.BlameResponseDto;
import com.ttasum.memorial.exception.blameText.BlamTextException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@Service
public class BlameTextPersistenceService {
    private final BlameTextLetterRepository blameTextLetterRepository;
    private final BlameTextLetterSentenceRepository blameTextLetterSentenceRepository;

    @Autowired
    public BlameTextPersistenceService(BlameTextLetterRepository blameTextLetterRepository, BlameTextLetterSentenceRepository blameTextLetterSentenceRepository) {
        this.blameTextLetterRepository = blameTextLetterRepository;
        this.blameTextLetterSentenceRepository = blameTextLetterSentenceRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveToDb(BlameResponseDto response) {
        BlameTextLetter savedLetter = blameTextLetterRepository.save(setBlameLetter(response));
        ArrayList<BlameTextLetterSentence> list = setBlameLetterSentence(response, savedLetter);
        blameTextLetterSentenceRepository.saveAll(list);
    }

    private BlameTextLetter setBlameLetter(BlameResponseDto response) {
        try {
            BlameTextLetter letter = new BlameTextLetter();
            letter.setSentence(response.getSentence());
            letter.setBlameLabel(response.getLabel());
            letter.setBlameConfidence(response.getConfidence());
            letter.setSentenceLine(response.getDetails().size());
            letter.setRegulation(0.7); // 규제 정도 계산 방식에 따라 설정(임의 설정)
            letter.setUpdateTime(LocalDateTime.now());
            letter.setBoardType("donation"); // 혹은 파라미터로 받아서 설정(임의 설정)

            return letter;
        } catch (NullPointerException e) {
            throw new BlamTextException("Null pointer exception.");
        }
    }

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
