//package com.ttasum.memorial.donationstory.service;
//
//import com.ttasum.memorial.domain.entity.donationStory.DonationStory;
//import com.ttasum.memorial.domain.repository.donationStory.DonationStoryRepository;
//import com.ttasum.memorial.dto.donationStory.DonationStoryUpdateRequestDto;
//import com.ttasum.memorial.exception.donationStory.DonationStoryNotFoundException;
//import com.ttasum.memorial.service.donationStory.DonationStoryService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class DonationStoryServiceUnitTest {
//
//    @Mock
//    private DonationStoryRepository repository;
//
//    @InjectMocks
//    private DonationStoryService service;
//
//    private DonationStory existingStory;
//    private DonationStoryUpdateRequestDto updateDto;
//
//    @BeforeEach
//    void setUp() {
//        // 예시 엔티티, DTO 준비
//        existingStory = DonationStory.builder()
//                .areaCode("110")
//                .title("초기 제목")
//                .donorName("홍길동")
//                .passcode("pass123")
//                .writer("작성자")
//                .anonymityFlag("N")
//                .readCount(0)
//                .contents("원본 내용")
//                .fileName("file.jpg")
//                .originalFileName("orig.jpg")
//                .writerId("user1")
//                .modifierId("user1")
//                .build();
//
//        // ReflectionTestUtils로 private id 필드 주입
//        ReflectionTestUtils.setField(existingStory, "id", 1);
//
//        // 업데이트 DTO 준비
//        updateDto = new DonationStoryUpdateRequestDto();
//        updateDto.setStoryTitle("수정된 제목");
//        updateDto.setDonorName("수정된 이름");
//        updateDto.setStoryContents("수정된 내용");
//    }
//
//    @Test
//    void updateStory_Success_changesEntityFields() {
//        // given
//        when(repository.findByIdAndDelFlag(1,"N")).thenReturn(Optional.of(existingStory));
//
//        // when
//        service.updateStory(1, updateDto);
//
//        // then: 엔티티 내부 필드가 DTO 값으로 변경되었는지 검증
//        assertEquals("수정된 제목", existingStory.getTitle());
//        assertEquals("수정된 이름", existingStory.getDonorName());
//        assertEquals("수정된 내용", existingStory.getContents());
//
//        // repository 조회만 호출, save()는 Dirty-Checking에 맡김
//        verify(repository, times(1)).findByIdAndDelFlag(1,"N"); // 특정 메서드가 정확히 몇 번 호출됐는지 검증
//        verifyNoMoreInteractions(repository); // 명시한 것 외에 다른 메서드 호출이 없었는지 검증
//    }
//
//    @Test
//    void updateStory_NotFound_throwsDonationStoryNotFoundException() {
//        // given
//        when(repository.findByIdAndDelFlag(1,"N")).thenReturn(Optional.empty());
//
//        // when & then
//        assertThrows(DonationStoryNotFoundException.class,
//                () -> service.updateStory(1, updateDto));
//
//        verify(repository, times(1)).findByIdAndDelFlag(1,"N");
//        verifyNoMoreInteractions(repository);
//    }
//}
