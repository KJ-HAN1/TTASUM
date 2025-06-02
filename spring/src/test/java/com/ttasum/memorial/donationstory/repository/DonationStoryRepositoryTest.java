//package com.ttasum.memorial.donationstory.repository;
//
//
//import com.ttasum.memorial.domain.entity.donationStory.DonationStory;
//import com.ttasum.memorial.domain.repository.donationStory.DonationStoryRepository;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.TestPropertySource;
//
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//@TestPropertySource(properties = {
//        // H2 인메모리 DB 세팅 (MySQL 모드 + 소문자 식별자 유지)
//        "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=MySQL;DATABASE_TO_UPPER=false",
//        "spring.datasource.driver-class-name=org.h2.Driver",
//        "spring.datasource.username=sa",
//        "spring.datasource.password=",
//        // H2 전용 Dialect 사용
//        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
//        // 엔티티 기반으로 스키마 자동 생성·삭제
//        "spring.jpa.hibernate.ddl-auto=create-drop"
//})
//public class DonationStoryRepositoryTest {
//
//    @Autowired
//    private DonationStoryRepository donationStoryRepository;
//
//    @Test
//    @DisplayName("기증후 스토리를 저장하고 다시 조회하면 값이 일치해야 한다")
//    void saveAndFindStroy() {
//        // 테스트용 스토리 객체 생성
//        DonationStory story = DonationStory.builder()
//                .areaCode("SEOUL")
//                .title("고인의 뜻을 이어갑니다")
//                .donorName("홍길동")
//                .passcode("pass1234")
//                .writer("코디네이터A")
//                .anonymityFlag("N")
//                .readCount(0)
//                .contents("기증을 통해 누군가에게 새로운 삶을 전했습니다.")
//                .fileName("img123.png")
//                .originalFileName("홍길동사진.jpg")
//                .writerId("coordinator1")
//                .modifierId("coordinator1")
//                .build();
//
//        // 저장 후 ID 기반으로 조회
//        DonationStory saved = donationStoryRepository.save(story);
//        // NPE 방지 optional 컨테이너 사용
//        Optional<DonationStory> result = donationStoryRepository.findById(saved.getId());
//
//        // 조회 결과 값 검증
//        assertThat(result).isPresent();
//        assertThat(result.get().getTitle()).isEqualTo("고인의 뜻을 이어갑니다");
//        assertThat(result.get().getPasscode()).isEqualTo("pass1234");
//
//    }
//}