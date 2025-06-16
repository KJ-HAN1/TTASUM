package com.ttasum.memorial.service.recipientLetter;

import com.ttasum.memorial.dto.recipientLetter.request.RecipientLetterRequestDto;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterDetailResponse;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterResponseDto;

public interface RecipientLetterService {

    //편지 등록
    RecipientLetterResponseDto createLetter(RecipientLetterRequestDto recipientLetterRequestDto);

    //편지 단건 조회
    RecipientLetterDetailResponse getLetterById(Integer letterSeq);
}
