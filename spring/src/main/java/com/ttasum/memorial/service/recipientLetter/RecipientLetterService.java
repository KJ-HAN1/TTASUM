package com.ttasum.memorial.service.recipientLetter;

import com.ttasum.memorial.dto.recipientLetter.request.RecipientLetterRequestDto;
import com.ttasum.memorial.dto.recipientLetter.request.RecipientLetterUpdateRequestDto;
import com.ttasum.memorial.dto.recipientLetter.request.RecipientLetterVerifyRequestDto;
import com.ttasum.memorial.dto.recipientLetter.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipientLetterService {

    //편지 등록
    RecipientLetterResponseDto createLetter(RecipientLetterRequestDto recipientLetterRequestDto);

    //편지 단건 조회
    RecipientLetterDetailResponse getLetterById(Integer letterSeq);

    //페이징 처리
    Page<RecipientLetterListResponseDto> getAllLetters(Pageable pageable);

    //편지 수정 인증(공통)
    boolean verifyPasscode(Integer letterSeq, String passcode);

    //편지 수정
    RecipientLetterUpdateResponseDto updateLetter(Integer letterSeq, RecipientLetterUpdateRequestDto recipientLetterUpdateRequestDto);

    //편지 삭제
//    RecipientLetterCommonResponseDto deleteLetter(RecipientLetterVerifyRequestDto deleteRequest);
    void deleteLetter(Integer letterSeq, RecipientLetterVerifyRequestDto deleteRequest);

}
