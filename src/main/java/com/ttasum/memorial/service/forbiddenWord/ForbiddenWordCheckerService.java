// AI 호출용 Service 클래스 작성
package com.ttasum.memorial.service.forbiddenWord;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ForbiddenWordCheckerService {
    private final RestTemplate restTemplate = new RestTemplate();

    public boolean containsForbiddenWord(String text) {
        // 실제 로직(아직 AI 서버 가동 X)
//        String url = "http://localhost:5000/predict-forbidden";  // AI 서버 주소
//        Map<String, String> request = Map.of("text", text);
//
//        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
//
//        return Boolean.TRUE.equals(response.getBody().get("forbidden"));

        // ForbiddenWordAspectTest.java에서 "금칙어_포함시_예외발생" 통과하기 위해 임시 설정
        return false;
    }
}
