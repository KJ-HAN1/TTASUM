package com.ttasum.memorial.service.heavenLetter;

import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetter;
import com.ttasum.memorial.domain.entity.heavenLetter.Memorial;
import com.ttasum.memorial.domain.repository.heavenLetter.MemorialRepository;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterRequest;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterUpdateRequest;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterVerifyRequest;
import com.ttasum.memorial.dto.heavenLetter.response.CommonResultResponse;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterResponse;
import com.ttasum.memorial.domain.repository.heavenLetter.HeavenLetterRepository;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterUpdateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
//final 필드에 대해 생성자 주입 자동 생성
@RequiredArgsConstructor
public class HeavenLetterServiceImpl implements HeavenLetterService {

    private final HeavenLetterRepository heavenLetterRepository;
    private final MemorialRepository memorialRepository;

    //등록
    @Transactional
    @Override
    public HeavenLetterResponse createLetter(HeavenLetterRequest heavenLetterRequest) {

        Memorial memorial = memorialRepository.findById(heavenLetterRequest.getDonateSeq())
                .orElseThrow(() -> new IllegalArgumentException("기증자 정보가 존재하지 않습니다"));

        HeavenLetter heavenLetter = HeavenLetter.builder()
                .donateSeq(memorial)
                .areaCode(heavenLetterRequest.getAreaCode())
                .letterTitle(heavenLetterRequest.getLetterTitle())
                .donorName(heavenLetterRequest.getDonorName())
                .letterPasscode(heavenLetterRequest.getLetterPasscode())
                .letterWriter(heavenLetterRequest.getLetterWriter())
                .anonymityFlag(heavenLetterRequest.getAnonymityFlag())
                .letterContents(heavenLetterRequest.getLetterContents())
                .fileName(heavenLetterRequest.getFileName())
                .orgFileName(heavenLetterRequest.getOrgFileName())
                .writerId(heavenLetterRequest.getWriterId())
                .build();

        heavenLetterRepository.save(heavenLetter);

        return HeavenLetterResponse.success();
    }

    //조회 - 단건
    @Transactional
    @Override
    public HeavenLetterResponse.HeavenLetterDetailResponse getLetterById(Integer letterSeq) {
        // findById: JPA 제공 메서드. Optional로 반환
        HeavenLetter heavenLetter = heavenLetterRepository.findById(letterSeq).get();

        //커맨드 메서드 사용
        heavenLetter.increaseReadCount();

        //엔티티 -> DTO
        return new HeavenLetterResponse.HeavenLetterDetailResponse(heavenLetter);
    }

    //조회 - 전체
    //new DTO(entity)	DTO 생성자에서 entity 객체 값을 추출
    //stream().map(...).collect(...)	리스트를 변환할 때 자주 사용하는 패턴

    @Transactional(readOnly = true)
    @Override
    public List<HeavenLetterResponse.HeavenLetterListResponse> getLetterList(){
        List<HeavenLetter> heavenLetters = heavenLetterRepository.findAll();

        //엔티티 -> DTO
        //List -> Stream 변환(가독성을 위해서)
        return heavenLetters.stream()
                .map(HeavenLetterResponse.HeavenLetterListResponse::new)
                .collect(Collectors.toList());
    }
    //수정 인증 (공통)
    @Transactional(readOnly = true)
    @Override
    public boolean verifyPasscode(Integer letterSeq, String passcode) {
        HeavenLetter heavenLetter = heavenLetterRepository.findById(letterSeq).orElseThrow();
        return heavenLetter.getLetterPasscode().equals(passcode);
    }
    //수정
    @Transactional
    @Override
    public HeavenLetterUpdateResponse updateLetter(HeavenLetterUpdateRequest heavenLetterUpdateRequest) {
        HeavenLetter heavenLetterUpdate = heavenLetterRepository.findById(heavenLetterUpdateRequest.getLetterSeq()).get();

        //기증자 외래키 변경(연결만 바꿔줌)
        Memorial memorial = memorialRepository.findById(heavenLetterUpdateRequest.getDonateSeq()).get();

        //내용수정
        heavenLetterUpdate.updateLetterContents(heavenLetterUpdateRequest, memorial);
        //응답 반환
        return HeavenLetterUpdateResponse.success();

    }
    //삭제
    @Transactional
    @Override
    public CommonResultResponse deleteLetter(HeavenLetterVerifyRequest deleteRequest){

        //편지 조회
        //비밀번호 인증
        if (!this.verifyPasscode(deleteRequest.getLetterSeq(), deleteRequest.getLetterPasscode())) {
            return CommonResultResponse.fail("비밀번호가 일치하지 않습니다.");
        }
        HeavenLetter heavenLetter = heavenLetterRepository.findById(deleteRequest.getLetterSeq()).get();

        //소프트 삭제 커멘드 사용
        heavenLetter.softDelete();

        return CommonResultResponse.success("편지가 정상적으로 삭제 되었습니다.");
    }
}