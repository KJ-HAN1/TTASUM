package com.ttasum.memorial.controller;


import com.ttasum.memorial.domain.entity.DonationStory;
import com.ttasum.memorial.dto.DonationStory.*;
import com.ttasum.memorial.service.DonationStoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
     * @param page 페이지
     * @param size 페이지당 개수
     * @return 스토리의 페이징된 목록
     */
    @GetMapping
    public ResponseEntity<PageResponse<DonationStory>> getStories(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size){
        log.info("/donationLetters?page={}&size={} - 기증후 스토리 목록 조회", page, size);
        Pageable pageable = PageRequest.of(page, size);
        // DB에서 페이징된 DonationStory 목록 조회
        Page<DonationStory> p = donationStoryService.getActiveStories(pageable);
        PageResponse<DonationStory> stories = new PageResponse<>(
                p.getContent(),
                p.getNumber(),
                p.getSize(),
                p.getTotalElements(),
                p.getTotalPages()
        );

        return ResponseEntity.ok(stories);
    }

    /**
     * 기증후 스토리 단건 조회
     * @param storySeq 조회할 스토리의 PK
     * @return 존재하는 경우 DonationStory 객체, 존재하지 않을 경우 404 응답
     */
    @GetMapping("/{storySeq}")
    public ResponseEntity<DonationStory> getStory(@PathVariable Integer storySeq){
        log.info("/donationLetters/storySeq={} - 단건 조회", storySeq);
        // 상태 코드 200 or 404
        return donationStoryService.findStoryById(storySeq)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 기증후 스토리 등록
     * @param dto 요청 본문
     * @return 생성된 DonationStory 객체와 201 CREATED 상태
     */
    @PostMapping
    public ResponseEntity<DonationStory> createStory(@RequestBody @Valid DonationStoryRequest dto){
        log.info("/donationLetters - 등록 요청: {}", dto);
        DonationStory story = DonationStory.builder()
                .areaCode(dto.getAreaCode())
                .title(dto.getTitle())
                .donorName(dto.getDonorName())
                .passcode(dto.getPasscode())
                .writer(dto.getWriter())
                .anonymityFlag(dto.getAnonymityFlag())
                .readCount(dto.getReadCount())
                .contents(dto.getContents())
                .fileName(dto.getFileName())
                .originalFileName(dto.getOriginalFileName())
                .writerId(dto.getWriterId())
                .modifierId(dto.getModifierId())
                .build();
        DonationStory saved = donationStoryService.saveStory(story);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /**
     * 기증후 스토리 수정
     * @param storySeq 수정할 스토리 ID
     * @param dto 수정 데이터
     * @return 200 OK or 404
     */
    @PutMapping("/{storySeq}")
    public ResponseEntity<Void> updateStory(@PathVariable Integer storySeq, @RequestBody @Valid DonationStoryUpdateRequestDto dto){
        log.info("/donationLetters/{} - 스토리 수정 요청", storySeq);
        // 서비스에서 예외 발생시 자동으로 404반환
        donationStoryService.updateStory(storySeq, dto);
        return ResponseEntity.ok().build();
    }

    /**
     * 기증후 스토리 수정 검증
     * @param storySeq 수정할 스토리 ID
     * @param dto 비밀번호 확인 요청 dto
     * @return 200 or 401(인증되지 않음)
     */
    @PostMapping("/{storySeq}/verifyPwd")
    public ResponseEntity<String> verifyStoryPasscode(@PathVariable Integer storySeq, @RequestBody @Valid DonationStoryPasswordVerifyDto dto){
        log.info("/donationLetters/{}/verifyPwd - 비밀번호 확인 요청", storySeq);

        boolean matched = donationStoryService.verifyStoryPasscode(storySeq, dto.getStoryPasscode());

        if(matched){
            return ResponseEntity.ok("비밀번호가 일치합니다.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다");
        }
    }

    /**
     * 기증후 스토리 삭제(소프트)
     * @param storySeq 수정할 스토리 ID
     * @param dto 삭제 요청 dto
     * @return 200 or 404
     */
    @PatchMapping("/{storySeq}")
    public ResponseEntity<Void> softDeleteStory(@PathVariable Integer storySeq, @RequestBody @Valid DonationStoryDeleteRequestDto dto){
        log.info("/donationLetters/{} - 스토리 삭제(소프트) 요청", storySeq);

        boolean isDeleted = donationStoryService.softDeleteStory(storySeq, dto.getModifierId());

        if(isDeleted){
            return ResponseEntity.ok().build(); // 성공 -> 200 반환
        }else {
            return ResponseEntity.notFound().build(); // 실패 -> 404 반환
        }
    }

}
