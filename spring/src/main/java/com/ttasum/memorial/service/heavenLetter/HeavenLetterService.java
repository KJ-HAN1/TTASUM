package com.ttasum.memorial.service.heavenLetter;

import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterRequestDto;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterUpdateRequestDto;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterVerifyRequestDto;
import com.ttasum.memorial.dto.heavenLetter.request.MemorialSearchRequestDto;
import com.ttasum.memorial.dto.heavenLetter.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface HeavenLetterService {

    //편지 등록
    HeavenLetterResponseDto createLetter(HeavenLetterRequestDto heavenLetterRequestDto);

    //추모관에서 등록하는 편지폼
    HeavenLetterFormResponseDto getFormWithDonor(Integer donateSeq);

    //편지 단건 조회
    HeavenLetterResponseDto.HeavenLetterDetailResponse getLetterById(Integer letterSeq);

//    //편지 전체 조회
//    List<HeavenLetterResponse.HeavenLetterListResponse> getLetterList();

    //편지 전체 조회(페이징 처리)
    Page<HeavenLetterResponseDto.HeavenLetterListResponse> getAllLetters(Pageable pageable);

    //편지 수정 인증(공통)
   boolean verifyPasscode(Integer letterSeq, String passcode);

    //편지 수정
    HeavenLetterUpdateResponsDto updateLetter(HeavenLetterUpdateRequestDto heavenLetterUpdateRequestDto);

    //편지 삭제
    CommonResultResponseDto deleteLetter(HeavenLetterVerifyRequestDto deleteRequest);

    //편지 전체 조회(검색 포함)
    Page<HeavenLetterResponseDto.HeavenLetterListResponse> searchLetters(String type, String keyword, Pageable pageable);

    //사진 업로드
    List<Map<String, String>> uploadFiles(List<MultipartFile> files, String subFolder) throws IOException;

    //기증자 검색
    Page<MemorialSearchResponseDto> searchDonors(MemorialSearchRequestDto memorialSearchRequest, Pageable pageable);
}