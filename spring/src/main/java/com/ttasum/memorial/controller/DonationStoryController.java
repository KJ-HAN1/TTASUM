package com.ttasum.memorial.controller;


import com.ttasum.memorial.dto.ApiResponse;
import com.ttasum.memorial.dto.DonationStory.*;
import com.ttasum.memorial.service.DonationStory.DonationStoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;


// 기증후 스토리 관련 API (등록, 조회, 목록 조회 등 제공)
@RestController
@RequestMapping("/donationLetters")
@RequiredArgsConstructor
@Slf4j
public class DonationStoryController {

    private final DonationStoryService donationStoryService;

    /**
     * 기증후 스토리 목록 조회(페이징)
     * @param page 페이지 번호 (0부터 시작)
     * @param size 페이지당 개수
     * @return DTO로 매핑된 스토리의 페이징된 목록
     */
    @GetMapping
    public ResponseEntity<PageResponse<DonationStoryResponseDto>> getStories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("/donationLetters?page={}&size={} - 기증후 스토리 목록 조회", page, size);
        Pageable pageable = PageRequest.of(page, size);

        // Service에서 DTO로 변환된 PageResponse 객체를 그대로 반환
        PageResponse<DonationStoryResponseDto> response = donationStoryService.getActiveStories(pageable);

        return ResponseEntity.ok(response);
    }

    /**
     * 기증후 스토리 단건 조회
     * @param storySeq 조회할 스토리의 PK
     * @return 존재하는 경우 DonationStory 객체, 존재하지 않을 경우 404 응답
     */
    @GetMapping("/{storySeq}")
    public ResponseEntity<DonationStoryResponseDto> getStory(@PathVariable Integer storySeq){
        log.info("/donationLetters/storySeq={} - 단건 조회", storySeq);
        DonationStoryResponseDto dto = donationStoryService.getStory(storySeq);
        return ResponseEntity.ok(dto);
    }

    /**
     * 기증후 스토리 등록
     * @param dto 요청 본문
     * @return 생성된 DonationStory 객체와 201 CREATED 상태
     */
    @PostMapping
    public ResponseEntity<ApiResponse> createStory(@RequestBody @Valid DonationStoryCreateRequestDto dto){
        log.info("/donationLetters - 등록 요청: {}", dto);
        donationStoryService.createStory(dto);
        return ResponseEntity.ok().body(ApiResponse.ok(
                HttpStatus.CREATED.value(),
                "스토리가 성곡적으로 등록되었습니다."
        ));
    }

    /**
     * 기증후 스토리 수정
     * @param storySeq 수정할 스토리 ID
     * @param dto 수정 데이터
     * @return 200 OK or 404
     */
    @PutMapping("/{storySeq}")
    public ResponseEntity<ApiResponse> updateStory(@PathVariable Integer storySeq, @RequestBody @Valid DonationStoryUpdateRequestDto dto){
        log.info("/donationLetters/{} - 스토리 수정 요청", storySeq);
        // 서비스에서 예외 발생시 자동으로 404반환
        donationStoryService.updateStory(storySeq, dto);
        return ResponseEntity.ok(ApiResponse.ok(
                HttpStatus.OK.value(),
                "스토리가 성공적으로 수정되었습니다."
        ));
    }

    /**
     * 비밀번호 검증 -> 수정용
     * @param storySeq 스토리 id
     * @param dto 사용자 입력을 담은 요청 dto
     * @return 비밀번호 검증 결과를 담은 200 OK 응답 (result: 1 또는 0, message: 결과 메시지 포함)
     */
    @PostMapping("/{storySeq}/verifyPwd")
    public ResponseEntity<DonationStoryPasswordVerifyResponseDto> verifyStoryPasscodeModification(
            @PathVariable Integer storySeq,
            @RequestBody @Valid DonationStoryPasswordVerifyDto dto){
        log.info("/donationLetters/{}/verifyPwd - 비밀번호 확인 요청(수정)", storySeq);
        return ResponseEntity.ok(donationStoryService.verifyStoryPasscode(storySeq,dto.getStoryPasscode()));
    }

    /**
     * 기증후 스토리 삭제(소프트) -> 비밀번호 검증에서 처리 후 진행
     * @param storySeq 수정할 스토리 ID
     * @param dto 삭제 요청 dto
     * @return 200 or 404
     */
//    @DeleteMapping("/{storySeq}")
//    public ResponseEntity<ApiResponse> softDeleteStory(@PathVariable Integer storySeq, @RequestBody @Valid DonationStoryDeleteRequestDto dto){
//        log.info("/donationLetters/{} - 스토리 삭제(소프트) 요청", storySeq);
//
//        donationStoryService.softDeleteStory(storySeq, dto.getModifierId());
//        return ResponseEntity.ok(ApiResponse.ok(
//                HttpStatus.OK.value(),
//                "스토리가 정상적으로 삭제 되었습니다."
//        ));
//    }

    /**
     * 기증후 스토리 삭제(소프트)
     * @param storySeq 수정할 스토리 ID
     * @param dto 삭제 요청 dto
     * @return 200 or 404
     */
    @DeleteMapping("/{storySeq}")
    public ResponseEntity<ApiResponse> softDeleteStory(@PathVariable Integer storySeq, @RequestBody @Valid DonationStoryDeleteRequestDto dto){
        log.info("/donationLetters/{} - 스토리 삭제(소프트) 요청", storySeq);

        boolean isDeleted = donationStoryService.softDeleteStory(storySeq, dto.getStoryPasscode(), dto.getModifierId());
        int result = isDeleted ? 1 : 0;
        String msg = isDeleted ? "스토리가 정상적으로 삭제 되었습니다." : "비밀번호가 일치하지 않습니다.";
        DonationStoryPasswordVerifyResponseDto response = new DonationStoryPasswordVerifyResponseDto(result, msg);

        if (!isDeleted) {
            // 비밀번호 불일치 시 400 Bad Request
            return ResponseEntity
                    .badRequest()
                    .body(ApiResponse.badRequest("비밀번호가 일치하지 않습니다."));
        }

        return ResponseEntity.ok(
                ApiResponse.ok(
                        HttpStatus.OK.value(),
                        "스토리가 정상적으로 삭제되었습니다."
                )
        );
    }

}
