package com.ttasum.memorial.controller.heavenLetter;

import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterRequestDto;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterUpdateRequestDto;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterVerifyRequestDto;
import com.ttasum.memorial.dto.heavenLetter.request.PageRequest;
import com.ttasum.memorial.dto.heavenLetter.response.CommonResultResponseDto;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterResponseDto;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterUpdateResponsDto;
import com.ttasum.memorial.service.heavenLetter.HeavenLetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/heavenLetters")
public class HeavenLetterController {

    private final HeavenLetterService heavenLetterService;

    //등록
    @PostMapping
    public ResponseEntity<HeavenLetterResponseDto> createLetter(
            @RequestBody @Valid HeavenLetterRequestDto creatRequest) {
        HeavenLetterResponseDto createResponse = heavenLetterService.createLetter(creatRequest);

        //상태코드 분기 처리
        HttpStatus status;
        //등록 성공
        if(createResponse.getCode() == 201){
            status = HttpStatus.CREATED;
        }
        //등록 실패
        else if (createResponse.getCode() == 400){
            status = HttpStatus.BAD_REQUEST;
        }
        else if (createResponse.getCode() == 500){
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } else{
            //400,500을 제외한 다른 예외를 기본 값으로 처리
            //안전성 확보의 이유
            status = HttpStatus.BAD_REQUEST;
        }
        return ResponseEntity.status(status).body(createResponse);
        }

    //단건 조회
    @GetMapping("/{letterSeq}")
    public ResponseEntity<HeavenLetterResponseDto.HeavenLetterDetailResponse> getLetterById(
            @PathVariable Integer letterSeq) {
        HeavenLetterResponseDto.HeavenLetterDetailResponse detailResponse = heavenLetterService.getLetterById(letterSeq);
        return ResponseEntity.status(HttpStatus.OK).body(detailResponse);
    }

    //목록(페이징 처리)
    @GetMapping
    public ResponseEntity<Page<HeavenLetterResponseDto.HeavenLetterListResponse>> getLetters(
            @ModelAttribute PageRequest pageRequest) {

        Pageable pageable = pageRequest.toPageable("letterSeq");
        Page<HeavenLetterResponseDto.HeavenLetterListResponse> result = heavenLetterService.getAllLetters(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    //편지 수정 인증
    @PostMapping("/{letterSeq}/verifyPwd")
    public ResponseEntity<CommonResultResponseDto> verifyPasscode(
            @PathVariable Integer letterSeq,
            @RequestBody @Valid HeavenLetterVerifyRequestDto heavenLetterVerifyRequestDto) {

        //verify -> verified 확인
        boolean verified = heavenLetterService.verifyPasscode(letterSeq , heavenLetterVerifyRequestDto.getLetterPasscode());

        // 위의 결과에 따른 bad response
        if(!verified){
            // return "redirect:/";
            return ResponseEntity.badRequest().body(CommonResultResponseDto.fail("비밀번호가 일치하지 않습니다."));
        }
        // return "letterUpdate";
        return ResponseEntity.status(HttpStatus.OK).body(CommonResultResponseDto.success("비밀번호가 일치합니다."));
    }
    //편지 수정 (letterUpdate.html)
    @PatchMapping("/{letterSeq}")
    public ResponseEntity<HeavenLetterUpdateResponsDto> updateLetter(
            //값을 자바 변수로 맵핑
            @PathVariable Integer letterSeq,
            @RequestBody @Valid HeavenLetterUpdateRequestDto heavenLetterUpdateRequestDto) {

        //letterSeq 값을 요청 DTO에 직접 주입
        heavenLetterUpdateRequestDto.setLetterSeq(letterSeq);

        HeavenLetterUpdateResponsDto heavenLetterUpdateResponsDto = heavenLetterService.updateLetter(heavenLetterUpdateRequestDto);

        //api명세서에 201로 나와있어 201로 지정했으나 코드리뷰 후 200로 바꿀 예정
        // return "redirect://";
        return ResponseEntity.status(HttpStatus.CREATED).body(heavenLetterUpdateResponsDto);
    }

    //편지 삭제
    @DeleteMapping("/{letterSeq}")
    public ResponseEntity<CommonResultResponseDto> deleteLetter(
            @RequestBody HeavenLetterVerifyRequestDto deleteRequest) {

        // DTO 객체 생성 후 setter로 값 설정
//        HeavenLetterVerifyRequest deleteRequest = new HeavenLetterVerifyRequest();
//        deleteRequest.setLetterSeq(letterSeq);

        CommonResultResponseDto deleteResponse = heavenLetterService.deleteLetter(deleteRequest);

        // 결과에 따라 상태코드 분기
        if (deleteResponse.getResult() == 1) {
            return ResponseEntity.ok(deleteResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(deleteResponse);
        }
    }
}



