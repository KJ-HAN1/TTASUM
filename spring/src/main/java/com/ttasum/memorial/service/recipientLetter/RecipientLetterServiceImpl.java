package com.ttasum.memorial.service.recipientLetter;


import com.ttasum.memorial.domain.entity.recipientLetter.RecipientLetter;
import com.ttasum.memorial.domain.repository.recipientLetter.RecipientLetterRepository;
import com.ttasum.memorial.dto.recipientLetter.request.RecipientLetterRequestDto;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterDetailResponse;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ttasum.memorial.exception.recipientLetter.RecipientLetterNotFoundException;

@Service
@RequiredArgsConstructor
public class RecipientLetterServiceImpl implements RecipientLetterService {

    private final RecipientLetterRepository recipientLetterRepository;

    //등록
    @Transactional
    @Override
    public RecipientLetterResponseDto createLetter(RecipientLetterRequestDto recipientLetterRequestDto) {

        RecipientLetter recipientLetter = RecipientLetter.builder()
                .letterWriter(recipientLetterRequestDto.getLetterWriter())
                .anonymityFlag(recipientLetterRequestDto.getAnonymityFlag())
                .letterPasscode(recipientLetterRequestDto.getLetterPasscode())
                .organCode(recipientLetterRequestDto.getOrganCode())
                .organEtc(recipientLetterRequestDto.getOrganEtc())
                .recipientYear(recipientLetterRequestDto.getRecipientYear())
                .storyTitle(recipientLetterRequestDto.getStoryTitle())
                .letterContents(recipientLetterRequestDto.getLetterContents())
                .orgFileName(recipientLetterRequestDto.getOrgFileName())
                .fileName(recipientLetterRequestDto.getFileName())
                .writerId(recipientLetterRequestDto.getWriterId())
                .build();


        recipientLetterRepository.save(recipientLetter);

        return RecipientLetterResponseDto.success();
    }
    //조회 - 단건
    @Transactional
    @Override
    public RecipientLetterDetailResponse getLetterById(Integer letterSeq) {
        // findById: JPA 제공 메서드. Optional로 반환
        RecipientLetter recipientLetter = recipientLetterRepository.findById(letterSeq)
                .orElseThrow(RecipientLetterNotFoundException::new);

        //커맨드 메서드 사용
        recipientLetter.increaseReadCount();

        //엔티티 -> DTO
        return new RecipientLetterDetailResponse(recipientLetter);
    }


}
