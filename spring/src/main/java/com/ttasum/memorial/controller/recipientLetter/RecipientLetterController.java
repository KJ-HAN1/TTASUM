package com.ttasum.memorial.controller.recipientLetter;

import com.ttasum.memorial.domain.repository.recipientLetter.RecipientLetterRepository;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterVerifyRequestDto;
import com.ttasum.memorial.dto.heavenLetter.request.PageRequest;
import com.ttasum.memorial.dto.heavenLetter.response.CommonResultResponseDto;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterResponseDto;
import com.ttasum.memorial.dto.recipientLetter.request.RecipientLetterRequestDto;
import com.ttasum.memorial.dto.recipientLetter.request.RecipientLetterVerifyRequestDto;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterCommonResponseDto;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterDetailResponse;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterListResponseDto;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterResponseDto;
import com.ttasum.memorial.service.recipientLetter.RecipientLetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    //단건 조회
    @GetMapping("/{letterSeq}")
    public ResponseEntity<RecipientLetterDetailResponse> getLetterById(
            @PathVariable Integer letterSeq) {
        RecipientLetterDetailResponse detailResponse = recipientLetterService.getLetterById(letterSeq);
        return ResponseEntity.status(HttpStatus.OK).body(detailResponse);
    }

    //목록(페이징 처리)
    @GetMapping
    public ResponseEntity<Page<RecipientLetterListResponseDto>> getLetters(
            @ModelAttribute PageRequest pageRequest) {

        Pageable pageable = pageRequest.toPageable("letterSeq");
        Page<RecipientLetterListResponseDto> result = recipientLetterService.getAllLetters(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    //편지 수정 인증
    @PostMapping("/{letterSeq}/verifyPwd")
    public ResponseEntity<RecipientLetterCommonResponseDto> verifyPasscode(
            @PathVariable Integer letterSeq,
            @RequestBody @Valid RecipientLetterVerifyRequestDto recipientLetterVerifyRequestDto) {

        recipientLetterService.verifyPasscode(letterSeq, recipientLetterVerifyRequestDto.getLetterPasscode());
        return ResponseEntity.status(HttpStatus.OK).body(RecipientLetterCommonResponseDto.success("비밀번호가 일치합니다."));
    }

}
