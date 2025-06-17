//package com.ttasum.memorial.donationstory.service;
//
//import com.ttasum.memorial.dto.donationStory.request.DonationStoryCreateRequestDto;
//import com.ttasum.memorial.dto.donationStory.response.DonationStoryResponseDto;
//import com.ttasum.memorial.service.donationStory.DonationStoryService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//// h2 사용
//@SpringBootTest(properties = {
//        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
//        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL",
//        "spring.datasource.driver-class-name=org.h2.Driver",
//        "spring.datasource.username=sa",
//        "spring.datasource.password=",
//        "spring.jpa.hibernate.ddl-auto=create-drop"
//})
//public class DonationStoryServiceTest {
//
//    @Autowired
//    private DonationStoryService donationStoryService;
//
//    private DonationStoryResponseDto saved;
//
//    @BeforeEach
//    void setUp() {
//        // 기증후 스토리 저장
//        DonationStoryCreateRequestDto dto = new DonationStoryCreateRequestDto();
//        dto.setAreaCode("110");
//        dto.setStoryTitle("테스트 스토리");
//        dto.setDonorName("홍길동");
//        dto.setStoryPasscode("testpass");
//        dto.setStoryWriter("관리자");
//        dto.setAnonymityFlag("N");
//        dto.setStoryContents("테스트 내용");
//        dto.setFileName("file.jpg");
//        dto.setOrgFileName("orig.jpg");
//        dto.setWriterId("user123");
//
//        // 서비스로 저장하고, 반환받은 DTO를 saved에 보관
//        saved = donationStoryService.createStory(dto);
//    }
//
//    @Test
//    @DisplayName("등록한 기증후 스토리를 ID로 조회할 수 있어야 함")
//    void findStoryById() {
//        // saved 는 @BeforeEach 에서 createStory() 호출로 채워둔 DTO
//        Integer id = saved.getStorySeq();
//
//        // when: DTO 반환 메서드 호출
//        DonationStoryResponseDto found = donationStoryService.getStory(id);
//
//        // then: DTO 필드 검증
//        assertThat(found).isNotNull();
//        assertThat(found.getStorySeq()).isEqualTo(id);
//        assertThat(found.getStoryTitle()).isEqualTo("테스트 스토리");
//    }
//
//
//}
