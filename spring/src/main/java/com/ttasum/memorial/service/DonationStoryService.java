package com.ttasum.memorial.service;


import com.ttasum.memorial.domain.entity.DonationStory.DonationStory;
import com.ttasum.memorial.domain.entity.DonationStory.DonationStoryComment;
import com.ttasum.memorial.domain.repository.DonationStory.DonationStoryCommentRepository;
import com.ttasum.memorial.domain.repository.DonationStory.DonationStoryRepository;
import com.ttasum.memorial.dto.DonationStory.*;
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
 * 기증후 스토리 게시글의 저장 및 조회 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
public class DonationStoryService {

    private final DonationStoryRepository donationStoryRepository;
    private final DonationStoryCommentRepository commentRepository;

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
    @Transactional(readOnly = true)
    public DonationStoryResponseDto getStory(Integer storySeq){
        DonationStory story = donationStoryRepository.findByIdAndDelFlag(storySeq, "N")
                .orElseThrow(() -> new DonationStoryNotFoundException(storySeq));

        List<DonationStoryComment> comments = commentRepository
                .findByStory_IdAndDelFlagOrderByWriteTimeAsc(storySeq, "N");

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
    public void updateStory(Integer storySeq, DonationStoryUpdateRequestDto dto) {

        DonationStory story = donationStoryRepository.findByIdAndDelFlag(storySeq, "N")
                .orElseThrow(() -> new DonationStoryNotFoundException(storySeq));

        // 엔티티 수정 - Dirty Checking 으로 변경 감지
        story.update(dto);

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
     * 소프트 삭제 기능
     * @param storySeq  검증할 id
     * @param modifierId 수정 id
     * @return 삭제 성공 여부
     */
    @Transactional
    public void softDeleteStory(Integer storySeq, String modifierId) {
        DonationStory story = donationStoryRepository.findByIdAndDelFlag(storySeq, "N")
                .orElseThrow(() -> new DonationStoryNotFoundException(storySeq));
        // 엔티티 내부에서 delFlag, modifierId, modifyTime 갱신
        story.delete(modifierId);
    }
}