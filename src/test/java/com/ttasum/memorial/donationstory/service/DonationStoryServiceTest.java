package com.ttasum.memorial.donationstory.service;

import com.ttasum.memorial.domain.entity.DonationStory;
import com.ttasum.memorial.service.DonationStoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// h2 사용
@SpringBootTest(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class DonationStoryServiceTest {

    @Autowired
    private DonationStoryService donationStoryService;

    private DonationStory saved;

    @BeforeEach
    void setUp() {
        // 기증후 스토리 저장
        DonationStory story = DonationStory.builder()
                .areaCode("DAEGU")
                .title("새로운 삶을 선물한 이야기")
                .donorName("이순신")
                .passcode("pw1234")
                .writer("코디네이터B")
                .anonymityFlag("Y")
                .readCount(0)
                .contents("이 기증을 통해 새로운 생명이 이어졌습니다.")
                .fileName("lee.png")
                .originalFileName("이순신기증.jpg")
                .writerId("admin")
                .modifierId("admin")
                .build();

        saved = donationStoryService.saveStory(story);
    }

    @Test
    @DisplayName("등록한 기증후 스토리를 ID로 조회할 수 있어야 함")
    void findStoryById() {
        // when
        Optional<DonationStory> found = donationStoryService.findStoryById(saved.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("새로운 삶을 선물한 이야기");
        assertThat(found.get().getPasscode()).isEqualTo("pw1234");
    }

}
