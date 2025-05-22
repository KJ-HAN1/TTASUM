package com.ttasum.memorial.service;


import com.ttasum.memorial.domain.entity.DonationStory.DonationStory;
import com.ttasum.memorial.domain.repository.DonationStory.DonationStoryCommentRepository;
import com.ttasum.memorial.domain.repository.DonationStory.DonationStoryRepository;
import com.ttasum.memorial.dto.DonationStory.DonationStoryCreateRequestDto;
import com.ttasum.memorial.dto.DonationStory.DonationStoryResponseDto;
import com.ttasum.memorial.dto.DonationStory.DonationStoryUpdateRequestDto;
import com.ttasum.memorial.dto.DonationStory.PageResponse;
import com.ttasum.memorial.exception.DonationStory.DonationStoryNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 기증후 스토리 겟글의 저장 및 조회 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class DonationStoryService {

    private final DonationStoryRepository donationStoryRepository;

    /**
     * 기증후 스토리 등록
     * @param dto 등록 요청용 dto
     * @return 엔티티 -> dto 변환후 반환
     */
    @Transactional
    public DonationStoryResponseDto createStory(DonationStoryCreateRequestDto dto){
        DonationStory story = dto.toEntity(); // DB 저장을 위해 Entity로 변환
        DonationStory saved = donationStoryRepository.save(story);
        return DonationStoryResponseDto.fromEntity(saved);
    }

    /**
     * 단건 스토리 조회
     * @param storySeq 스토리 ID
     * @return 엔티티 -> dto 변환후 반환
     */
    @Transactional
    public DonationStoryResponseDto getStory(Integer storySeq){
        DonationStory story = donationStoryRepository.findById(storySeq)
                .orElseThrow(() -> new DonationStoryNotFoundException(storySeq));
        return DonationStoryResponseDto.fromEntity(story);
    }

    /**
     * 활성화된 스토리 페이징 조회
     * @param pageable 페이징 처리 객체 -> Page<T> 반환
     * @return Page 객체 -> dto 반환
     */
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
    public void updateStory(Integer storySeq, DonationStoryUpdateRequestDto dto) {

        DonationStory story = donationStoryRepository.findById(storySeq)
                .orElseThrow(() -> new DonationStoryNotFoundException(storySeq));

        // 엔티티 수정 - Dirty Checking 으로 변경 감지
        story.update(dto);

        // Dirty Checking 으로 자동 반영
        // donationStoryRepository.save(story);
    }

    /**
     * 수정 전 비밀번호 검증
     * @param storySeq 검증할 ID
     * @param inputPasscode 사용자가 입력한 비밀번호
     * @return 비밀번호 일치 여부(true/false)
     */
    @Transactional(readOnly = true)
    public boolean verifyStoryPasscode(Integer storySeq, String inputPasscode) {
        // id로 스토리 조회
        Optional<DonationStory> optionalStory  = donationStoryRepository.findById(storySeq);

        // Optional이 비어 있지 않으면 비밀번호 비교
        return optionalStory.map(story -> inputPasscode.equals(story.getPasscode()))
                .orElse(false);
    }

    /**
     * 소프트 삭제 기능
     * @param storySeq  검증할 id
     * @param modifierId 수정 id
     * @return 삭제 성공 여부
     */
    @Transactional
    public boolean softDeleteStory(Integer storySeq, String modifierId) {
        DonationStory story = donationStoryRepository.findById(storySeq)
                .orElseThrow(() -> new DonationStoryNotFoundException(storySeq));

        // 엔티티 내부에서 delFlag, modifierId, modifyTime 갱신
        story.delete(modifierId);
        // 더티 체킹 기능으로 생략 가능
        // donationStoryRepository.save(story);

        return true;
    }
}