package com.ttasum.memorial.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ChatService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ChatService(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public Mono<ResponseEntity<String>> getLLMResponse(String question) {
        return webClient.post()
                .uri("/chat/")
                .bodyValue(Map.of("question", question))
                .exchangeToMono(response -> {
                        // 정상적 흐름일 경우: 200 OK + 본문 내용 반환
                        if (response.statusCode().is2xxSuccessful()) {
                            return response.bodyToMono(String.class)
                                .map(result -> ResponseEntity.status(HttpStatus.OK)
                                                                    .body(result));
                        }
                        // 400, 500 (WebClientResponseException) 발생한 경우: 상태 코드 + 본문 내용 반환
                        // FastAPI에서 발생한 HTTPException이 JSON 응답으로 반환되며 파싱 처리
                        return response.bodyToMono(String.class)
                            .map(errorBody -> {
                                try {
                                    Map<String, String> errorMap = objectMapper.readValue(errorBody, Map.class);
                                    String detailMsg = errorMap.getOrDefault("detail", "LLM 응답 중 오류가 발생했습니다."); // 찾는 키가 없다면 기본 값 반환
                                    return ResponseEntity.status(response.statusCode())
                                                        .body(detailMsg);
                                } catch (Exception e) {
                                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                                        .body("서버 응답 파싱 중 오류가 발생했습니다.");
                                }
                            });
                    }
                ).onErrorResume(e ->
                    // 그 외 Exception 처리
                    Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("내부 네트워크 서버 오류, 타임 아웃 등으로 일시적인 문제가 발생했습니다.")
                ));
    }
}
