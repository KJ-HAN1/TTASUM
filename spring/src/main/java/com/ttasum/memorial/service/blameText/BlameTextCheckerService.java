package com.ttasum.memorial.service.blameText;

import com.ttasum.memorial.domain.entity.blameText.BlameTextLetter;
import com.ttasum.memorial.domain.entity.blameText.BlameTextLetterSentence;
import com.ttasum.memorial.domain.repository.blameText.BlameTextLetterRepository;
import com.ttasum.memorial.domain.repository.blameText.BlameTextLetterSentenceRepository;
import com.ttasum.memorial.dto.blameText.BlameResponseDto;
import com.ttasum.memorial.exception.blameText.BlamTextException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

@Service
public class BlameTextCheckerService {
    private final RestTemplate restTemplate = new RestTemplate();
    private BlameTextPersistenceService persistenceService;

    @Autowired
    public BlameTextCheckerService(BlameTextPersistenceService persistenceService){
        this.persistenceService = persistenceService;
    }

    public BlameResponseDto analyze(String sentence) throws Exception {
        String url = "http://localhost:8000/predict-blameText";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (sentence == null) {
            throw new BlamTextException("비난 텍스트 검사 요청 json에 null 값이 전달되었습니다.");
        }

        Map<String, String> requestMap = Map.of("sentence", sentence);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestMap, headers);

        ResponseEntity<BlameResponseDto> response = restTemplate.postForEntity(url, request, BlameResponseDto.class);
        return response.getBody();
    }

    public BlameResponseDto analyzeAndSave(String sentence) throws Exception {
        BlameResponseDto response = analyze(sentence);
        persistenceService.saveToDb(response); // 트랜잭션 분리됨
        return response;
    }
}
