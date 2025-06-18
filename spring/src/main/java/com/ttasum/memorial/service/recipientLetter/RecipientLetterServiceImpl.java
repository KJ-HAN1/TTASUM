package com.ttasum.memorial.service.recipientLetter;


import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetter;
import com.ttasum.memorial.domain.entity.recipientLetter.RecipientLetter;
import com.ttasum.memorial.domain.repository.recipientLetter.RecipientLetterRepository;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterResponseDto;
import com.ttasum.memorial.dto.recipientLetter.request.RecipientLetterRequestDto;
import com.ttasum.memorial.dto.recipientLetter.request.RecipientLetterUpdateRequestDto;
import com.ttasum.memorial.dto.recipientLetter.request.RecipientLetterVerifyRequestDto;
import com.ttasum.memorial.dto.recipientLetter.response.*;
import com.ttasum.memorial.exception.common.Conflict.AlreadyDeletedException;
import com.ttasum.memorial.exception.common.badRequest.InvalidPasscodeException;
import com.ttasum.memorial.exception.common.badRequest.PathVariableMismatchException;
import com.ttasum.memorial.exception.common.notFound.NotFoundException;
import com.ttasum.memorial.exception.heavenLetter.InvalidPasswordException;
//import com.ttasum.memorial.util.OrganCodeUtil;
//import com.ttasum.memorial.util.OrganResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ttasum.memorial.exception.recipientLetter.RecipientLetterNotFoundException;

import java.util.Locale;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipientLetterServiceImpl implements RecipientLetterService {

    private final RecipientLetterRepository recipientLetterRepository;

    //등록
    @Transactional
    @Override
    public RecipientLetterResponseDto createLetter(RecipientLetterRequestDto recipientLetterRequestDto) {

//        // OrganCode 변환 처리
//        String input = recipientLetterRequestDto.getOrganCode(); // ex: "신장"
//        String resolvedCode = OrganCodeUtil.resolveCodeByName(input);
//        String organEtc = null;
//        if ("ORGAN000".equals(resolvedCode)) {
//            organEtc = input;
//        }

//        OrganResult organResult = OrganCodeUtil.resolveCodeAndEtc(recipientLetterRequestDto.getOrganCode());

        RecipientLetter recipientLetter = RecipientLetter.builder()
                .letterWriter(recipientLetterRequestDto.getLetterWriter())
                .anonymityFlag(recipientLetterRequestDto.getAnonymityFlag())
                .letterPasscode(recipientLetterRequestDto.getLetterPasscode())
//                .organCode(organResult.getCode())
//                .organEtc(organResult.getEtc())
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

        RecipientLetter recipientLetter = recipientLetterRepository
                .findByLetterSeqAndDelFlag(letterSeq, "N")
                .orElseThrow(() -> new NotFoundException("해당 수혜자 편지를 찾을 수 없습니다."));

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
            throw new PathVariableMismatchException("수혜자 편지 번호와 요청 번호가 일치하지 않습니다.");
        }

        RecipientLetter recipientLetter = recipientLetterRepository.findById(letterSeq)
                .orElseThrow(RecipientLetterNotFoundException::new);

        // 편지 내용 수정
        recipientLetter.updateLetterContents(recipientLetterUpdateRequestDto); // 예: 내부에서 title, contents 세팅

        return RecipientLetterUpdateResponseDto.success();
    }

    //삭제
    @Transactional
    @Override
    public void deleteLetter(Integer letterSeq, RecipientLetterVerifyRequestDto deleteRequest) {

        // 경로와 본문 값 불일치 (400 Bad Request)
        if (!letterSeq.equals(deleteRequest.getLetterSeq())) {
            throw new PathVariableMismatchException("수혜자 편지 번호와 요청 번호가 일치하지 않습니다.");
        }

        // 리소스를 찾을 수 없음 (404 Not Found)
        RecipientLetter recipientLetter = recipientLetterRepository.findById(letterSeq)
                .orElseThrow(() -> new NotFoundException("해당 수혜자 편지를 찾을 수 없습니다."));

        // 비밀번호 인증 실패 시 예외 발생(400)
        if (!verifyPasscode(letterSeq, deleteRequest.getLetterPasscode())) {
            throw new InvalidPasscodeException();
        }

        // 이미 삭제된 리소스에 대한 요청 (409 Conflict)
        if ("Y".equals(recipientLetter.getDelFlag())) {
            throw new AlreadyDeletedException("이미 삭제된 수혜자 편지입니다.");
        }

        // 5. 삭제 처리
        recipientLetter.softDelete();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<RecipientLetterListResponseDto> searchLetters(String type, String keyword, Pageable pageable) {
        Logger log = LoggerFactory.getLogger(RecipientLetterServiceImpl.class);

        log.info("[HeavenLetter 검색] type={}, keyword={}, page={}, size={}",
                type, keyword, pageable.getPageNumber(), pageable.getPageSize());

        Specification<RecipientLetter> spec = notDeleted();

        if ("title".equalsIgnoreCase(type)) {
            spec = spec.and(titleContains(keyword));
        } else if ("contents".equalsIgnoreCase(type)) {
            spec = spec.and(contentsContains(keyword));
        } else if ("all".equalsIgnoreCase(type)) {
            spec = spec.and(allFieldsContains(keyword));
        }

        Page<RecipientLetter> result = recipientLetterRepository.findAll(spec, pageable);
        log.info("[검색 결과] 총 건수: {}", result.getTotalElements());

        return result.map(RecipientLetterListResponseDto::fromEntity);
    }

    private Specification<RecipientLetter> notDeleted() {
        return (root, query, cb) -> cb.equal(root.get("delFlag"), "N");
    }

    private Specification<RecipientLetter> titleContains(String keyword) {
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("storyTitle")), "%" + (keyword == null ? "" : keyword.trim().toLowerCase(Locale.ROOT)) + "%");
    }

    private Specification<RecipientLetter> contentsContains(String keyword) {
        return (root, query, cb) ->
                cb.like(root.get("letterContents"), "%" + (keyword == null ? "" : keyword.trim()) + "%");
    }

    private Specification<RecipientLetter> allFieldsContains(String keyword) {

        String raw = (keyword == null ? "" : keyword.trim());
        String lower = raw.toLowerCase(Locale.ROOT);

        // 한글 장기명 매핑 시도
//        Optional<OrganCode> organCodeName = OrganCode.fromName(raw);


        return (root, query, cb) ->
                cb.or(
                        cb.like(cb.lower(root.get("storyTitle")), "%" + keyword.toLowerCase(Locale.ROOT) + "%"),
                        cb.like(root.get("letterContents"), "%" + keyword + "%"),
//                        cb.like(cb.lower(root.get("organCode")), "%" + keyword.toLowerCase(Locale.ROOT) + "%"),
//                        cb.like(cb.lower(root.get("organEtc")), "%" + keyword.toLowerCase(Locale.ROOT) + "%"),
                        cb.like(cb.lower(root.get("recipientYear")), "%" + keyword.toLowerCase(Locale.ROOT) + "%")
                );
    }
}



