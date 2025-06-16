package com.ttasum.memorial.service.recipientLetter;


import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetter;
import com.ttasum.memorial.domain.entity.recipientLetter.RecipientLetter;
import com.ttasum.memorial.domain.repository.recipientLetter.RecipientLetterRepository;
import com.ttasum.memorial.dto.recipientLetter.request.RecipientLetterRequestDto;
import com.ttasum.memorial.dto.recipientLetter.request.RecipientLetterUpdateRequestDto;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterDetailResponse;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterListResponseDto;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterResponseDto;
import com.ttasum.memorial.dto.recipientLetter.response.RecipientLetterUpdateResponseDto;
import com.ttasum.memorial.exception.heavenLetter.HeavenLetterNotFoundException;
import com.ttasum.memorial.exception.heavenLetter.InvalidPasswordException;
import com.ttasum.memorial.exception.recipientLetter.RecipientLetterMismatchException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    //페이징처리
    @Transactional
    @Override
    public Page<RecipientLetterListResponseDto> getAllLetters(Pageable pageable) {
        return recipientLetterRepository.findAllByDelFlag("N", pageable)
                .map(RecipientLetterListResponseDto::fromEntity);
    }
    //수정 인증 (공통)
    @Transactional(readOnly = true)
    @Override
    public boolean verifyPasscode(Integer letterSeq, String passcode) {
        RecipientLetter recipientLetter = recipientLetterRepository.findById(letterSeq)
                .orElseThrow(RecipientLetterNotFoundException::new);

        if (!recipientLetter.getLetterPasscode().equals(passcode)) {
            throw new InvalidPasswordException();
        }

        return true;
    }
    //수정
    @Transactional
    @Override
    public RecipientLetterUpdateResponseDto updateLetter(Integer letterSeq, RecipientLetterUpdateRequestDto recipientLetterUpdateRequestDto) {

        if (!letterSeq.equals(recipientLetterUpdateRequestDto.getLetterSeq())) {
            throw new RecipientLetterMismatchException();
        }

        RecipientLetter recipientLetter = recipientLetterRepository.findById(letterSeq)
                .orElseThrow(RecipientLetterNotFoundException::new);

        // 편지 내용 수정
        recipientLetter.updateLetterContents(recipientLetterUpdateRequestDto); // 예: 내부에서 title, contents 세팅

        return RecipientLetterUpdateResponseDto.success();
    }
}

