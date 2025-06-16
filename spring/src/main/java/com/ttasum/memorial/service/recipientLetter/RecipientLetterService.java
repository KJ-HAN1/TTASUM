package com.ttasum.memorial.service.recipientLetter;

import com.ttasum.memorial.dto.recipientLetter.request.RecipientLetterRequestDto;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterResponseDto;

public interface RecipientLetterService {

    //편지 등록
    RecipientLetterResponseDto createLetter(RecipientLetterRequestDto recipientLetterRequestDto);
}
