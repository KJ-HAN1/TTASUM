package com.ttasum.memorial.controller.recipientLetter;

import com.ttasum.memorial.domain.repository.recipientLetter.RecipientLetterRepository;
import com.ttasum.memorial.dto.recipientLetter.request.RecipientLetterRequestDto;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterResponseDto;
import com.ttasum.memorial.service.recipientLetter.RecipientLetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recipientLetters")
public class RecipientLetterController {

    private final RecipientLetterService recipientLetterService;


    //등록
    @PostMapping
    public ResponseEntity<RecipientLetterResponseDto> createLetter(
            @RequestBody @Valid RecipientLetterRequestDto createRequest) {
        RecipientLetterResponseDto createResponse = recipientLetterService.createLetter(createRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(createResponse);
    }
}
