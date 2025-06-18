package com.ttasum.memorial.service.donationStory;


import com.ttasum.memorial.domain.entity.donationStory.DonationStory;
import com.ttasum.memorial.domain.entity.donationStory.DonationStoryComment;
import com.ttasum.memorial.domain.repository.donationStory.DonationStoryCommentRepository;
import com.ttasum.memorial.domain.repository.donationStory.DonationStoryRepository;
import com.ttasum.memorial.dto.donationStory.request.DonationStoryCreateRequestDto;
import com.ttasum.memorial.dto.donationStory.request.DonationStoryUpdateRequestDto;
import com.ttasum.memorial.dto.donationStory.response.DonationStoryPasswordVerifyResponseDto;
import com.ttasum.memorial.dto.donationStory.response.DonationStoryResponseDto;
import com.ttasum.memorial.exception.common.badRequest.InvalidKeywordLengthException;
import com.ttasum.memorial.exception.common.badRequest.InvalidPaginationParameterException;
import com.ttasum.memorial.exception.common.badRequest.InvalidSearchFieldException;
import com.ttasum.memorial.exception.common.badRequest.CaptchaVerificationFailedException;
import com.ttasum.memorial.exception.donationStory.DonationStoryNotFoundException;
import com.ttasum.memorial.service.common.CaptchaVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 기증후 스토리 게시글의 저장 및 조회 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class DonationStoryService {
    // 허용 가능한 정렬 필드 집합
    private static final Set<String> ALLOWED_SEARCH_FIELDS = Set.of("title", "contents", "all");
    private static final int MIN_KEYWORD_LENGTH = 2;
    private static final int MAX_KEYWORD_LENGTH = 100;

    private final DonationStoryRepository donationStoryRepository;
    private final DonationStoryCommentRepository commentRepository;
    private final CaptchaVerifier captchaVerifier;

    /**
     * 검색 + 페이징 조회 (추가된 메서드)
     * @param searchField ("title", "contents", "all")
     * @param keyword     검색어 (null 또는 빈 문자열 -> 전체 조회)
     * @param pageable    페이징 정보
     * @return 엔티티 -> DTO 변환 후 반환
     */
    @Transactional(readOnly = true)
    public Page<DonationStoryResponseDto> searchStories(String searchField, String keyword, Pageable pageable) {
        // 페이지 번호/크기 방어
        if (pageable.getPageNumber() < 0 || pageable.getPageSize() < 1) {
            throw new InvalidPaginationParameterException("유효하지 않은 페이지 번호 또는 크기입니다. page >= 0, size >= 1 이어야 합니다.");
        }

        // searchField 검증
        if (searchField != null && !ALLOWED_SEARCH_FIELDS.contains(searchField)) {
            throw new InvalidSearchFieldException("유효하지 않은 검색 대상입니다: " + searchField);
        }

        // keyword 검증
        if (keyword != null && !keyword.isBlank()) {
            String trimmed = keyword.trim();
            if (trimmed.length() < MIN_KEYWORD_LENGTH || trimmed.length() > MAX_KEYWORD_LENGTH) {
                throw new InvalidKeywordLengthException("검색어는 " + MIN_KEYWORD_LENGTH + "자 이상 " + MAX_KEYWORD_LENGTH + "자 이하로 입력해야 합니다.");
            }
            keyword = trimmed;
        }
        // 커스텀 리포지터리 메서드를 호출하여 Page<> 반환
        Page<DonationStory> page = donationStoryRepository.searchStories(searchField, keyword, pageable);

        // 엔티티 -> DTO 변환
        return page.map(DonationStoryResponseDto::fromEntity);
    }

    /**
     * 기증후 스토리 등록
     * @param dto 등록 요청용 dto
     * @return 엔티티 -> dto 변환후 반환
     */
    @Transactional
    public DonationStoryResponseDto createStory(DonationStoryCreateRequestDto dto){
        if (!captchaVerifier.verifyCaptcha(dto.getCaptchaToken())) {
            throw new CaptchaVerificationFailedException();
        }
        DonationStory story = dto.toEntity(); // DB 저장을 위해 Entity로 변환
        DonationStory saved = donationStoryRepository.save(story);
        return DonationStoryResponseDto.fromEntity(saved);
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
     * 기증후 스토리 수정
     * @param storySeq 스토리 ID
     * @param dto 수정 요청 dto
     */
    @Transactional
    public void updateStory(Integer storySeq, DonationStoryUpdateRequestDto dto) {
        DonationStory story = donationStoryRepository.findByIdAndDelFlag(storySeq, "N")
                .orElseThrow(() -> new DonationStoryNotFoundException(storySeq));

        // 엔티티 수정 - Dirty Checking 으로 변경 감지
        story.update(dto);
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