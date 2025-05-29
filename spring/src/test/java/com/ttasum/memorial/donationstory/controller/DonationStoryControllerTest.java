package com.ttasum.memorial.donationstory.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ttasum.memorial.controller.DonationStoryController;
import com.ttasum.memorial.domain.entity.DonationStory.DonationStory;
import com.ttasum.memorial.dto.DonationStory.DonationStoryDeleteRequestDto;
import com.ttasum.memorial.dto.DonationStory.DonationStoryResponseDto;
import com.ttasum.memorial.dto.DonationStory.DonationStoryUpdateRequestDto;
import com.ttasum.memorial.dto.DonationStory.PageResponse;
import com.ttasum.memorial.exception.DonationStory.DonationStoryNotFoundException;
import com.ttasum.memorial.service.DonationStory.DonationStoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

// REST API 규약에 맞는 응답을 하고 있는지 검증
@WebMvcTest(controllers = DonationStoryController.class)
public class DonationStoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DonationStoryService donationStoryService;

    @Autowired
    private ObjectMapper objectMapper;

    // 목록 조회(페이징 처리)
    @Test
    @DisplayName("GET /donationLetters → PageResponse 반환")
    void getStories_ReturnsPageOfDto() throws Exception {
        // 1) 테스트용 엔티티 및 DTO 준비
        DonationStory story = DonationStory.builder()
                .areaCode("110")
                .title("희망의 이야기")
                .donorName("홍길동")
                .passcode("abcd1234")
                .writer("관리자")
                .anonymityFlag("N")
                .readCount(0)
                .contents("이곳에 스토리 내용을 입력합니다.")
                .fileName("story.jpg")
                .originalFileName("original_story.jpg")
                .writerId("user123")
                .modifierId("user123")
                .build();
        ReflectionTestUtils.setField(story, "id", 1);

        DonationStoryResponseDto dto = DonationStoryResponseDto.fromEntity(story);

        // 2) Pageable과 PageResponse 리턴값 모킹
        Pageable pageable = PageRequest.of(0, 5);
        PageResponse<DonationStoryResponseDto> pageResponse =
                new PageResponse<>(
                        List.of(dto),
                        0,      // page number
                        5,      // page size
                        1,      // total elements
                        1       // total pages
                );

        when(donationStoryService.getActiveStories(pageable))
                .thenReturn(pageResponse);

        // 3) MockMvc로 GET 요청 검증
        mockMvc.perform(get("/donationLetters")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].storySeq").value(1))
                .andExpect(jsonPath("$.content[0].title").value("희망의 이야기"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
    }

    // 단건 조회 성공 테스트
    @Test
    void getStory_ReturnsOk() throws Exception {
        // dto 준비
        DonationStoryResponseDto dto = DonationStoryResponseDto.builder()
                .storySeq(2)
                .storyTitle("단건제목")
                .donorName("홍길동")
                .storyWriter("작성자")
                .anonymityFlag("Y")
                .readCount(0)
                .storyContents("내용")
                .fileName("")
                .orgFileName("")
                .writerId("u1")
                .modifierId("u1")
                .build();
        // 서비스 모킹: getStory → dto 반환
        when(donationStoryService.getStory(2))
                .thenReturn(dto);

        // 요청 및 검증
        mockMvc.perform(get("/donationLetters/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.storySeq").value(2))
                .andExpect(jsonPath("$.title").value("단건제목"));
    }

    // 단건 조회 실패 테스트
    @Test
    void getStory_ReturnsNotFound() throws Exception {
        when(donationStoryService.getStory(999))
                .thenThrow(new DonationStoryNotFoundException(999));

        mockMvc.perform(get("/donationLetters/999"))
                .andExpect(status().isNotFound());
    }

    // 소프트 삭제 테스트
    @Test
    void softDeleteStory_ReturnsOk() throws Exception {
        DonationStoryDeleteRequestDto dto = new DonationStoryDeleteRequestDto();
        dto.setModifierId("user123");

        Mockito.doReturn(true)
                .when(donationStoryService).softDeleteStory(1, "user123");

        mockMvc.perform(delete("/donationLetters/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void softDeleteStory_ReturnsNotFound() throws Exception {
        DonationStoryDeleteRequestDto dto = new DonationStoryDeleteRequestDto();
        dto.setModifierId("user123");

        Mockito.doReturn(false)
                .when(donationStoryService).softDeleteStory(1, "user123");

        mockMvc.perform(delete("/donationLetters/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    // 스토리 수정 성공 테스트
    @Test
    void updateStory_ReturnsOk() throws Exception {
        // DTO 생성
        DonationStoryUpdateRequestDto dto = new DonationStoryUpdateRequestDto();

        dto.setAreaCode("110");
        dto.setStoryTitle("희망의 이야기");
        dto.setDonorName("홍길동");
        dto.setStoryPasscode("abcd1234");
        dto.setStoryWriter("관리자");
        dto.setAnonymityFlag("N");
        dto.setReadCount(0);
        dto.setStoryContents("이곳에 스토리 내용을 입력합니다.");
        dto.setFileName("story.jpg");
        dto.setOrgFileName("original_story.jpg");
        dto.setWriterId("user123");
        dto.setModifierId("user123");

        // 서비스 메서드 Mock 설정: 예외 없이 정상 종료
        Mockito.doNothing()
                .when(donationStoryService).updateStory(Mockito.eq(1), Mockito.any());

        // MockMvc 로 HTTP PUT 요청 수행
        mockMvc.perform(put("/donationLetters/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                // 기대 결과: HTTP 200 OK
                .andExpect(status().isOk());
    }

    @Test
    void updateStory_ReturnsNotFound() throws Exception {
        // DTO 준비
        DonationStoryUpdateRequestDto dto = new DonationStoryUpdateRequestDto();
        dto.setAreaCode("110");
        dto.setStoryTitle("제목");
        dto.setDonorName("기증자");
        dto.setStoryPasscode("pppp1234");
        dto.setStoryWriter("작성자");
        dto.setAnonymityFlag("N");
        dto.setReadCount(0);
        dto.setStoryContents("내용");
        dto.setFileName("file.jpg");
        dto.setOrgFileName("orig.jpg");
        dto.setWriterId("user123");
        dto.setModifierId("user123");

        // 서비스에서 NotFound 예외 발생하도록 Mock 설정
        Mockito.doThrow(new DonationStoryNotFoundException(1))
                .when(donationStoryService)
                .updateStory(Mockito.eq(1), Mockito.any(DonationStoryUpdateRequestDto.class));

        // 요청 수행 및 404 검증
        mockMvc.perform(put("/donationLetters/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

}
