package com.ttasum.memorial.donationstory.service;

import com.ttasum.memorial.domain.entity.DonationStory.DonationStory;
import com.ttasum.memorial.domain.repository.DonationStory.DonationStoryRepository;
import com.ttasum.memorial.dto.DonationStory.DonationStoryResponseDto;
import com.ttasum.memorial.dto.DonationStory.PageResponse;
import com.ttasum.memorial.service.DonationStoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DonationStoryPageServiceTest {

    @Mock
    private DonationStoryRepository donationStoryRepository;

    @InjectMocks
    private DonationStoryService donationStoryService;

    @Test
    @DisplayName("활성화된 스토리 목록을 페이지 단위로 조회하면 DTO로 변환되어 반환된다")
    void getActiveStoriesTest() {
        // given: 페이징 요청 정보
        Pageable pageable = PageRequest.of(0, 10);

        // and: 테스트용 엔티티 준비
        DonationStory story = DonationStory.builder()
                .areaCode("110")
                .title("희망의 이야기")
                .donorName("홍길동")
                .passcode("abcd1234")
                .writer("관리자")
                .anonymityFlag("N")
                .readCount(5)
                .contents("내용")
                .fileName("file.jpg")
                .originalFileName("orig.jpg")
                .writerId("user123")
                .modifierId("user123")
                .build();
        ReflectionTestUtils.setField(story, "id", 1);

        // and: repository 모킹
        Page<DonationStory> mockPage =
                new PageImpl<>(List.of(story), pageable, 1);
        when(donationStoryRepository
                .findByDelFlagOrderByWriteTimeDesc("N", pageable))
                .thenReturn(mockPage);

        // when: 서비스 호출
        PageResponse<DonationStoryResponseDto> response =
                donationStoryService.getActiveStories(pageable);

        // then: DTO 페이지 응답 검증
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getPage()).isZero();
        assertThat(response.getSize()).isEqualTo(10);
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getTotalPages()).isEqualTo(1);

        // and: DTO 필드 매핑 확인
        DonationStoryResponseDto dto = response.getContent().get(0);
        assertThat(dto.getStorySeq()).isEqualTo(story.getId());
        assertThat(dto.getStoryTitle()).isEqualTo(story.getTitle());
        assertThat(dto.getDonorName()).isEqualTo(story.getDonorName());
        assertThat(dto.getAnonymityFlag()).isEqualTo(story.getAnonymityFlag());
        assertThat(dto.getReadCount()).isEqualTo(story.getReadCount());
    }
}
