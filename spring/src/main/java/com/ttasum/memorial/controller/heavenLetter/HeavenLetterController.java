package com.ttasum.memorial.controller.heavenLetter;

import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterRequest;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterUpdateRequest;
import com.ttasum.memorial.dto.heavenLetter.request.HeavenLetterVerifyRequest;
import com.ttasum.memorial.dto.heavenLetter.response.CommonResultResponse;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterResponse;
import com.ttasum.memorial.dto.heavenLetter.response.HeavenLetterUpdateResponse;
import com.ttasum.memorial.service.heavenLetter.HeavenLetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/heavenLetters")
public class HeavenLetterController {

    private final HeavenLetterService heavenLetterService;

    //등록
    @PostMapping
    public ResponseEntity<HeavenLetterResponse> createLetter(
            @RequestBody @Valid HeavenLetterRequest creatRequest) {
        HeavenLetterResponse createResponse = heavenLetterService.createLetter(creatRequest);

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
    public ResponseEntity<HeavenLetterResponse.HeavenLetterDetailResponse> getLetterById(
            @PathVariable Integer letterSeq) {
        HeavenLetterResponse.HeavenLetterDetailResponse detailResponse = heavenLetterService.getLetterById(letterSeq);
        return ResponseEntity.status(HttpStatus.OK).body(detailResponse);
    }

    //목록(전체) 조회
    @GetMapping
    public ResponseEntity<List<HeavenLetterResponse.HeavenLetterListResponse>> getLetterList() {
        List<HeavenLetterResponse.HeavenLetterListResponse> listResponse = heavenLetterService.getLetterList();
        return ResponseEntity.status(HttpStatus.OK).body(listResponse);
    }
    //편지 수정 인증
    @PostMapping("/{letterSeq}/verifyPwd")
    public ResponseEntity<CommonResultResponse> verifyPasscode(
            @PathVariable Integer letterSeq,
            @RequestBody @Valid HeavenLetterVerifyRequest heavenLetterVerifyRequest) {

        //verify -> verified 확인
        boolean verified = heavenLetterService.verifyPasscode(letterSeq , heavenLetterVerifyRequest.getLetterPasscode());

        // 위의 결과에 따른 bad response
        if(!verified){
            // return "redirect:/";
            return ResponseEntity.badRequest().body(CommonResultResponse.fail("비밀번호가 일치하지 않습니다."));
        }
        // return "letterUpdate";
        return ResponseEntity.status(HttpStatus.OK).body(CommonResultResponse.success("비밀번호가 일치합니다."));
    }
    //편지 수정 (letterUpdate.html)
    @PatchMapping ("/{letterSeq}")
    public ResponseEntity<HeavenLetterUpdateResponse> updateLetter(
            //값을 자바 변수로 맵핑
            @PathVariable Integer letterSeq,
            @RequestBody @Valid HeavenLetterUpdateRequest heavenLetterUpdateRequest) {

        //letterSeq 값을 요청 DTO에 직접 주입
        heavenLetterUpdateRequest.setLetterSeq(letterSeq);

        HeavenLetterUpdateResponse heavenLetterUpdateResponse = heavenLetterService.updateLetter(heavenLetterUpdateRequest);

        //api명세서에 201로 나와있어 201로 지정했으나 코드리뷰 후 200로 바꿀 예정
        // return "redirect://";
        return ResponseEntity.status(HttpStatus.CREATED).body(heavenLetterUpdateResponse);
    }
    //편지 삭제
    @DeleteMapping("/{letterSeq}")
    public ResponseEntity<CommonResultResponse> deleteLetter(
            @RequestBody HeavenLetterVerifyRequest deleteRequest) {

        // DTO 객체 생성 후 setter로 값 설정
//        HeavenLetterVerifyRequest deleteRequest = new HeavenLetterVerifyRequest();
//        deleteRequest.setLetterSeq(letterSeq);

        CommonResultResponse deleteResponse = heavenLetterService.deleteLetter(deleteRequest);

        // 결과에 따라 상태코드 분기
        if (deleteResponse.getResult() == 1) {
            return ResponseEntity.ok(deleteResponse);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(deleteResponse);
        }
    }
}



