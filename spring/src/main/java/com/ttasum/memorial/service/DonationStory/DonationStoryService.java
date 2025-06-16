package com.ttasum.memorial.service.DonationStory;


import com.ttasum.memorial.domain.entity.DonationStory.DonationStory;
import com.ttasum.memorial.domain.entity.DonationStory.DonationStoryComment;
import com.ttasum.memorial.domain.entity.blameText.BlameTextLetter;
import com.ttasum.memorial.domain.repository.DonationStory.DonationStoryCommentRepository;
import com.ttasum.memorial.domain.repository.DonationStory.DonationStoryRepository;
import com.ttasum.memorial.dto.ApiResponse;
import com.ttasum.memorial.dto.DonationStory.*;
import com.ttasum.memorial.dto.forbiddenWord.ReviewRequestDto;
import com.ttasum.memorial.exception.CaptchaVerificationFailedException;
import com.ttasum.memorial.exception.DonationStory.DonationStoryNotFoundException;
import com.ttasum.memorial.service.common.CaptchaVerifier;
import com.ttasum.memorial.service.forbiddenWord.TestReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 기증후 스토리 게시글의 저장 및 조회 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class DonationStoryService {

    private final DonationStoryRepository donationStoryRepository;
    private final DonationStoryCommentRepository commentRepository;
    private final CaptchaVerifier captchaVerifier;

    /**
     * 기증후 스토리 등록
     * @param dto 등록 요청용 dto
     */
    @Transactional
    public DonationStory createStory(DonationStoryCreateRequestDto dto){
        if (!captchaVerifier.verifyCaptcha(dto.getCaptchaToken())) {
            throw new CaptchaVerificationFailedException();
        }

        DonationStory story = dto.toEntity(); // DB 저장을 위해 Entity로 변환
        return donationStoryRepository.save(story);


    }

    /**
     * 단건 스토리 조회
     * @param storySeq 스토리 ID
     * @return 엔티티 -> dto 변환후 반환
     */
    @Transactional(readOnly = true)
    public DonationStoryResponseDto getStory(Integer storySeq){
        DonationStory story = donationStoryRepository.findByIdAndDelFlag(storySeq, "N")
                .orElseThrow(() -> new DonationStoryNotFoundException(storySeq));

        List<DonationStoryComment> comments = commentRepository
                .findByStory_IdAndDelFlagOrderByWriteTimeAsc(storySeq, "N");

        story.increaseReadCount();

        return DonationStoryResponseDto.fromEntity(story, comments);
    }

    /**
     * 활성화된 스토리 페이징 조회
     * @param pageable 페이징 처리 객체 -> Page<T> 반환
     * @return Page 객체 -> dto 반환
     */
    @Transactional(readOnly = true)
    public PageResponse<DonationStoryResponseDto> getActiveStories(Pageable pageable) {
        // JPA가 반환한 Page<DonationStory> 조회
        Page<DonationStory> page = donationStoryRepository.findByDelFlagOrderByWriteTimeDesc("N", pageable);

        // 엔티티 dto 변환 작업
        List<DonationStoryResponseDto> dtoList = page.getContent().stream()
                .map(DonationStoryResponseDto::fromEntity)
                .collect(Collectors.toList());

        // PageResponse<> 생성 후 반환
        return new PageResponse<>(
                dtoList,
                page.getNumber(),        // 현재 페이지 번호
                page.getSize(),          // 페이지 크기
                page.getTotalElements(), // 전체 요소 개수
                page.getTotalPages()     // 전체 페이지 수
        );
    }

    /**
     * 기증후 스토리 수정
     * @param storySeq 스토리 ID
     * @param dto 수정 요청 dto
     */
    @Transactional
    public DonationStory updateStory(Integer storySeq, DonationStoryUpdateRequestDto dto) {

        DonationStory story = donationStoryRepository.findByIdAndDelFlag(storySeq, "N")
                .orElseThrow(() -> new DonationStoryNotFoundException(storySeq));

        // 엔티티 수정 - Dirty Checking 으로 변경 감지
        story.update(dto);
        return story;
        // Dirty Checking 으로 자동 반영
        // donationStoryRepository.save(story);
    }

    /**
     * 비밀번호 검증
     * @param storySeq 스토리 id
     * @param inputPasscode 입력받은 비밀번호
     * @return 응답용 dto
     */
    @Transactional(readOnly = true)
    public DonationStoryPasswordVerifyResponseDto verifyStoryPasscode(Integer storySeq, String inputPasscode) {
        Optional<DonationStory> optionalStory = donationStoryRepository.findByIdAndDelFlag(storySeq, "N");

        boolean matched = optionalStory.map(story -> inputPasscode.equals(story.getPasscode()))
                .orElse(false);

        int result = matched ? 1 : 0;
        String message = matched ? "비밀번호가 일치합니다." : "비밀번호가 일치하지 않습니다.";

        return new DonationStoryPasswordVerifyResponseDto(result, message);
    }

    /**
     * 스토리 소프트 삭제 (delFlag = 'Y')
     * @param storySeq      삭제할 스토리 ID
     * @param inputPasscode 사용자가 입력한 스토리 비밀번호
     * @param modifierId    삭제 요청자 ID (로그용)
     * @return 삭제 성공(true) 또는 비밀번호 불일치(false)
     */
    @Transactional
    public boolean softDeleteStory(Integer storySeq, String inputPasscode, String modifierId) {
        DonationStory story = donationStoryRepository.findByIdAndDelFlag(storySeq, "N")
                .orElseThrow(() -> new DonationStoryNotFoundException(storySeq));
        // 비밀번호 검증
        if (!story.getPasscode().equals(inputPasscode)) {
            return false;
        }
        // 엔티티 내부에서 delFlag, modifierId, modifyTime 갱신
        story.delete(modifierId);

        return true;
    }
}