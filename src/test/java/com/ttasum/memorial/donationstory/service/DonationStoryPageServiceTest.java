package com.ttasum.memorial.donationstory.service;

import com.ttasum.memorial.domain.entity.DonationStory;
import com.ttasum.memorial.service.DonationStoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

// h2 사용
@SpringBootTest(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL;DATABASE_TO_UPPER=false",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class DonationStoryPageServiceTest {

    @Autowired
    private DonationStoryService donationStoryService;

    @Test
    @DisplayName("스토리 목록 페이징 조회가 가능해야 한다")
    void getActiveStoriesTest() {
        // 첫 페이지 5개 조회
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<DonationStory> stories = donationStoryService.getActiveStories(pageRequest);

        assertThat(stories).isNotNull();
        assertThat(stories.getContent().size()).isLessThanOrEqualTo(10);
        // 삭제되지 않은 게시글만 반환되었는지 확인
        stories.forEach(story -> assertThat(story.getDelFlag()).isEqualTo("N"));
    }
}
