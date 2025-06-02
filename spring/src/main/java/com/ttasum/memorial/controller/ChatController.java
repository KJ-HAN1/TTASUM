package com.ttasum.memorial.controller;

import com.ttasum.memorial.dto.ChatDto;
import com.ttasum.memorial.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/chat")
    public Mono<ResponseEntity<String>> getChatResponse(@RequestBody ChatDto question) {
        // 입력 유효성 검사: null, 빈 공백만 입력할 경우
        if(question.getQuestion() == null || question.getQuestion().trim().isEmpty()) {
            return Mono.just(
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body("빈 공백만 입력할 수 없습니다. 좀 더 구체적으로 입력해 주세요.")
            );
        }
        return chatService.getLLMResponse(question.getQuestion());
    }
}
