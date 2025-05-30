package com.ttasum.memorial.service.heavenLetter;

import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterRequest;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterUpdateRequest;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterVerifyRequest;
import com.ttasum.memorial.dto.heavenLetter.response.CommonResultResponse;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterResponse;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterUpdateResponse;

import java.util.List;

public interface HeavenLetterService {

    //편지 등록
    HeavenLetterResponse createLetter(HeavenLetterRequest heavenLetterRequest);

    //편지 단건 조회
    HeavenLetterResponse.HeavenLetterDetailResponse getLetterById(Integer letterSeq);

    //편지 전체 조회
    List<HeavenLetterResponse.HeavenLetterListResponse> getLetterList();

    //편지 수정 인증(공통)
   boolean verifyPasscode(Integer letterSeq, String passcode);

    //편지 수정
    HeavenLetterUpdateResponse updateLetter(HeavenLetterUpdateRequest heavenLetterUpdateRequest);

    //편지 삭제
    CommonResultResponse deleteLetter(HeavenLetterVerifyRequest deleteRequest);
}