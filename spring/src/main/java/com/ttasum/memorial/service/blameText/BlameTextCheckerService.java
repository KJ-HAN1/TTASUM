package com.ttasum.memorial.service.blameText;

import com.ttasum.memorial.dto.BlameResponseDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class BlameTextCheckerService {
    private final RestTemplate restTemplate = new RestTemplate();

    public int checkBlameText(String sentence) {
        String url = "http://localhost:8000/predict-blameText";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (sentence == null) {
            throw new IllegalArgumentException("비난 텍스트 요청에 null 값이 전달되었습니다.");
        }

        Map<String, String> requestMap = Map.of("sentence", sentence);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestMap, headers);

        ResponseEntity<BlameResponseDto> response = restTemplate.postForEntity(url, request, BlameResponseDto.class);

        Object label = response.getBody().getLabel();

        // 혹시 모를 casting 오류를 대비해서 Integer.parseInt 처리
        return Integer.parseInt(label.toString());
    }

}
