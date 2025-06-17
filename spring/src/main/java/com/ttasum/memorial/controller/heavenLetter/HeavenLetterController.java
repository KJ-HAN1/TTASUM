package com.ttasum.memorial.controller.heavenLetter;

import com.ttasum.memorial.domain.entity.heavenLetter.HeavenLetter;
import com.ttasum.memorial.dto.common.ApiResponse;
import com.ttasum.memorial.dto.heavenLetter.request.*;
import com.ttasum.memorial.dto.heavenLetter.response.*;
import com.ttasum.memorial.service.forbiddenWord.TestReviewService;
import com.ttasum.memorial.service.heavenLetter.HeavenLetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.ttasum.memorial.domain.type.BoardType.DONATION;
import static com.ttasum.memorial.domain.type.BoardType.HEAVEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/heavenLetters")
public class HeavenLetterController {

    private final HeavenLetterService heavenLetterService;
    private final TestReviewService testReviewService;

    //등록
    @PostMapping
    public ResponseEntity<?> createLetter(
            @RequestBody @Valid HeavenLetterRequestDto creatRequest) {
        HeavenLetter Story = heavenLetterService.createLetter(creatRequest);

        // 비난글 AI 필터링 추가
        testReviewService.saveBoardFromBlameTable(Story, true, HEAVEN.getType());

        return ResponseEntity.ok().body(ApiResponse.ok(
                HttpStatus.CREATED.value(),
                "스토리가 성공적으로 등록되었습니다."
        ));
    }

    //추모관에서 등록하는 편지폼
    @GetMapping("/new/{donateSeq}")
    public ResponseEntity<HeavenLetterFormResponseDto> getFormWithDonor(@PathVariable Integer donateSeq) {
        return ResponseEntity.ok(heavenLetterService.getFormWithDonor(donateSeq));
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

        heavenLetterService.verifyPasscode(letterSeq, heavenLetterVerifyRequestDto.getLetterPasscode());
        return ResponseEntity.status(HttpStatus.OK).body(CommonResultResponseDto.success("비밀번호가 일치합니다."));
    }

    //편지 수정 (letterUpdate.html)
    @PatchMapping("/{letterSeq}")
    public ResponseEntity<?> updateLetter(
            //값을 자바 변수로 맵핑
            @PathVariable Integer letterSeq,
            @RequestBody @Valid HeavenLetterUpdateRequestDto heavenLetterUpdateRequestDto) {

        //letterSeq 값을 요청 DTO에 직접 주입
        heavenLetterUpdateRequestDto.setLetterSeq(letterSeq);

        HeavenLetter Story = heavenLetterService.updateLetter(heavenLetterUpdateRequestDto);

        // 비난글 AI 필터링 추가
        testReviewService.saveBoardFromBlameTable(Story, true, HEAVEN.getType());

        // return "redirect://";
        return ResponseEntity.ok(ApiResponse.ok(
                HttpStatus.OK.value(),
                "스토리가 성공적으로 수정되었습니다."
        ));
    }

    //편지 삭제
    @DeleteMapping("/{letterSeq}")
    public ResponseEntity<CommonResultResponseDto> deleteLetter(
            @RequestBody HeavenLetterVerifyRequestDto deleteRequest) {

        CommonResultResponseDto deleteResponse = heavenLetterService.deleteLetter(deleteRequest);
        return ResponseEntity.ok(deleteResponse);
    }

    //검색
    @GetMapping("/search")
    public ResponseEntity<Page<HeavenLetterResponseDto.HeavenLetterListResponse>> searchLetters(
            @RequestParam(defaultValue = "전체") String type,
            @RequestParam(defaultValue = "") String keyword,
            @ModelAttribute PageRequest pageRequest) {

        // 한글 타입을 영문으로 매핑
        type = switch (type) {
            case "제목" -> "title";
            case "내용" -> "contents";
            case "전체" -> "all";
            default -> "all";
        };

        Pageable pageable = pageRequest.toPageable("letterSeq");
        Page<HeavenLetterResponseDto.HeavenLetterListResponse> result =
                heavenLetterService.searchLetters(type, keyword, pageable);
        return ResponseEntity.ok(result);
    }
    //사진 업로드
    @PostMapping("/upload-image")
    public ResponseEntity<List<Map<String, String>>> uploadFiles(
            @RequestParam("files") List<MultipartFile> files) throws IOException {
        List<Map<String, String>> resultList = heavenLetterService.uploadFiles(files, "heavenLetter");
        return ResponseEntity.ok(resultList);
    }
    //기증자 검색
    @GetMapping("/donate_pop.do")
    public ResponseEntity<Page<MemorialSearchResponseDto>> searchDonors(
            @ModelAttribute MemorialSearchRequestDto memorialSearchRequest,
            @ModelAttribute PageRequest pageRequest
    ) {
        Pageable pageable = pageRequest.toPageable("donateDate");
        Page<MemorialSearchResponseDto> result =
                heavenLetterService.searchDonors(memorialSearchRequest, pageable);

        return ResponseEntity.ok(result);
    }
}





