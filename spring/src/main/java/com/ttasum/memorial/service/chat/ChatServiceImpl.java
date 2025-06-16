package com.ttasum.memorial.service.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttasum.memorial.dto.chat.ChatApiResponse;
import com.ttasum.memorial.dto.chat.ChatDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ChatServiceImpl(WebClient webClient) {
        this.webClient = webClient;
        objectMapper = new ObjectMapper();
    }

    /**
     * 챗봇 응답을 처리하는 핵심 비즈니스 로직.
     * 비동기 논블로킹 방식({@link WebClient})을 통해 FastAPI로부터 응답을 받아 처리합니다.
     *
     * @param inputMsg 사용자 입력 메시지 DTO
     * @return {@code Mono<ResponseEntity<ChatApiResponse>>} 챗봇 응답 DTO
     */
    @Override
    public Mono<ResponseEntity<ChatApiResponse>> getLLMResponse(ChatDto inputMsg) {
        log.info("[LLM_API_RESPONSE_INIT] 파이썬 챗봇 API 호출 시작: \"{}\"", inputMsg.getQuestion());
        try {
            return webClient.post()
                    .uri("/chat/")
                    .bodyValue(objectMapper.writeValueAsString(inputMsg))
                    .exchangeToMono(ChatServiceImpl::getCustomResponseFromPython)
                    .doOnSuccess(response -> log.info("[LLM_API_RESPONSE_RECEIVED] 파이썬 챗봇 API 응답 수신 완료: {}", response.getStatusCode()))
                    .onErrorResume(ChatServiceImpl::getDefaultErrorResponse);
        } catch (JsonProcessingException e) {
            return getJsonProcessingError();
        }
    }

    private static Mono<ResponseEntity<ChatApiResponse>> getJsonProcessingError() {
        log.warn("[JSON_SERIALIZATION_ERROR] 입력 메시지 직렬화 실패");
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ChatApiResponse.error(HttpStatus.BAD_REQUEST.value(), "사용자의 입력을 처리하는 과정에서 문제가 발생했습니다.")));
    }

    private static Mono<ResponseEntity<ChatApiResponse>> getDefaultErrorResponse(Throwable e) {
        // 그 외 Exception (WebClientRequestException) 처리
        log.error("[WEBCLIENT_COMMUNICATION_ERROR] 파이썬 챗봇 API 통신 중 에러 발생: {}", e.getMessage(), e);
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ChatApiResponse.error()));
    }

    private static Mono<ResponseEntity<ChatApiResponse>> getCustomResponseFromPython(ClientResponse response) {
        // 정상적 흐름일 경우: 200 OK + 본문 내용 반환
        if (response.statusCode().is2xxSuccessful()) {
            return getSuccessResponse(response);
        }
        // 400, 500 (WebClientResponseException) 발생한 경우: 상태 코드 + 오류 내용 반환
        return getErrorResponse(response);
    }

    private static Mono<ResponseEntity<ChatApiResponse>> getErrorResponse(ClientResponse response) {
        return response.bodyToMono(ChatApiResponse.class)
                .map(errorBody -> {
                            log.error("[PYTHON_ERROR_BODY_PARSING] 파이썬 챗봇 API 에러 응답 본문 파싱 완료");
                            return ResponseEntity.status(response.statusCode())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(ChatApiResponse.error(errorBody.getCode(), errorBody.getMessage()));
                        }
                );
    }

    private static Mono<ResponseEntity<ChatApiResponse>> getSuccessResponse(ClientResponse response) {
        log.info("[PYTHON_SUCCESS_BODY_PARSING] 파이썬 챗봇 API 성공 응답 본문 파싱 완료");
        return response.bodyToMono(ChatApiResponse.class)
                .map(result -> {
                    System.out.println("ChatServiceImpl.getSuccessResponse");
                    return ResponseEntity.status(HttpStatus.OK)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(ChatApiResponse.ok(result.getData()));
                });
    }
}
