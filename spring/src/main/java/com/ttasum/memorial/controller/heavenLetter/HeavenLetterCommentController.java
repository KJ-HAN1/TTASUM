package com.ttasum.memorial.controller.heavenLetter;

import com.ttasum.memorial.dto.heavenLetter.request.CommonCommentRequestDto;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterCommentResponseDto;
import com.ttasum.memorial.service.heavenLetter.HeavenLetterCommentService;
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
@RequestMapping("/heavenLetters/{letterSeq}/comments")
public class HeavenLetterCommentController {

    private final HeavenLetterCommentService heavenLetterCommentService;

    //등록
    @PostMapping
    public ResponseEntity<HeavenLetterCommentResponseDto> createComment(
            @RequestBody @Valid CommonCommentRequestDto.CreateCommentRequest createCommentRequest){
        HeavenLetterCommentResponseDto createCommentResponse = heavenLetterCommentService.createComment(createCommentRequest);

        //상태코드 분기 처리
        HttpStatus status;

        //등록 성공
        if(createCommentResponse.getCode() == 201){
            status = HttpStatus.CREATED;
        }
        //등록 실패
        else if (createCommentResponse.getCode() == 400) {
            status = HttpStatus.BAD_REQUEST;
        }else if (createCommentResponse.getCode() == 500) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }else{
            status = HttpStatus.BAD_REQUEST;
        }
        return ResponseEntity.status(status).body(createCommentResponse);
    }

}
