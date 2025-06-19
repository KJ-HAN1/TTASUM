package com.ttasum.memorial.rememberance.repository;

import com.ttasum.memorial.domain.entity.memorial.Memorial;
import com.ttasum.memorial.domain.entity.memorial.MemorialReply;
import com.ttasum.memorial.domain.repository.memorial.MemorialRepository;
import com.ttasum.memorial.dto.memorial.response.MemorialResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest(
        properties = {
                "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1",
                "spring.datasource.driver-class-name=org.h2.Driver",
                "spring.datasource.username=sa",
                "spring.datasource.password=",
                "spring.jpa.hibernate.ddl-auto=create-drop"
        }
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@EntityScan("com.ttasum.memorial.domain.entity")
@EnableJpaRepositories("com.ttasum.memorial.domain.repository.memorial")
class MemorialRepositoryTest {

    @Autowired
    private MemorialRepository memorialRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("findByDonateSeqAndDelFlag: delFlag='N' 이면 Optional.of 반환")
    void findByDonateSeqAndDelFlag_FlagN_ReturnsEntity() {
        // given
        Memorial m = Memorial.builder()
                .donateSeq(null)
                .donorName("김테스트")
                .anonymityFlag("N")
                .donateTitle("제목")
                .areaCode("001")
                .contents("내용")
                .fileName(null)
                .orgFileName(null)
                .writer("tester")
                .donateDate("20240101")
                .genderFlag("M")
                .donateAge(30)
                .writerId("u1")
                .donorBirthdate(LocalDate.of(1990,1,1))
                .delFlag("N")
                .build();
        // persist -> generate ID
        em.persist(m);
        em.flush();
        Integer seq = m.getDonateSeq();

        // when
        Optional<Memorial> found = memorialRepository.findByDonateSeqAndDelFlag(seq, "N");

        // then
        assertThat(found)
                .isPresent()
                .get()
                .extracting(Memorial::getDonorName)
                .isEqualTo("김테스트");
    }

    @Test
    @DisplayName("findByDonateSeqAndDelFlag: delFlag!='N' 이면 Optional.empty")
    void findByDonateSeqAndDelFlag_FlagY_ReturnsEmpty() {
        // given
        Memorial m = Memorial.builder()
                .donateSeq(null)
                .donorName("박테스트")
                .anonymityFlag("N")
                .donateTitle("제목")
                .areaCode("001")
                .contents("내용")
                .fileName(null)
                .orgFileName(null)
                .writer("tester")
                .donateDate("20240101")
                .genderFlag("F")
                .donateAge(25)
                .writerId("u2")
                .donorBirthdate(LocalDate.of(1995,5,5))
                .delFlag("Y")
                .build();
        em.persist(m);
        em.flush();

        // when
        Optional<Memorial> found = memorialRepository.findByDonateSeqAndDelFlag(m.getDonateSeq(), "N");

        // then
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("findByFilter: 필터 없이 조회하면 전체 DTO Page 반환 (replyCount=0)")
    void findByFilter_NoCriteria_ReturnsAll() {
        // given: 두 건 등록
        Memorial m1 = Memorial.builder()
                .donorName("A씨")
                .anonymityFlag("N")
                .donateTitle("T1")
                .areaCode("001")
                .contents("C1")
                .fileName(null)
                .orgFileName(null)
                .writer("u1")
                .donateDate("20240101")
                .genderFlag("M")
                .donateAge(40)
                .writerId("u1")
                .delFlag("N")
                .donorBirthdate(LocalDate.of(1980,1,1))
                .build();
        Memorial m2 = Memorial.builder()
                .donorName("B씨")
                .anonymityFlag("N")
                .donateTitle("T2")
                .areaCode("002")
                .contents("C2")
                .fileName(null)
                .orgFileName(null)
                .writer("u2")
                .donateDate("20240202")
                .genderFlag("F")
                .donateAge(35)
                .writerId("u2")
                .delFlag("N")
                .donorBirthdate(LocalDate.of(1985,2,2))
                .build();
        em.persist(m1);
        em.persist(m2);
        em.flush();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("donateSeq").ascending());

        // when
        Page<MemorialResponseDto> page = memorialRepository.findByFilter(
                null, null, null, pageable);

        // then
        List<MemorialResponseDto> content = page.getContent();
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(content)
                .extracting("donorName")
                .containsExactly("A씨", "B씨");
        // 댓글 미등록이므로 모두 0
        assertThat(content)
                .extracting(MemorialResponseDto::getCommentCount)   // -> List<Long>
                .allMatch(count -> count == 0L);
    }

    @Test
    @DisplayName("findByFilter: donorName 조건으로 필터링")
    void findByFilter_NameFilter_ReturnsMatching() {
        // given: 세 건 중 하나만 '홍' 포함
        Memorial m1 = Memorial.builder()
                .donorName("홍길동")
                .anonymityFlag("N")
                .donateTitle("T1")
                .areaCode("001")
                .contents("C1")
                .fileName(null)
                .orgFileName(null)
                .writer("u1")
                .donateDate("20240101")
                .genderFlag("M")
                .donateAge(50)
                .writerId("u1")
                .delFlag("N")
                .donorBirthdate(LocalDate.of(1970,1,1))
                .build();
        Memorial m2 = Memorial.builder()
                .donorName("김철수")
                .anonymityFlag("N")
                .donateTitle("T2")
                .areaCode("002")
                .contents("C2")
                .fileName(null)
                .orgFileName(null)
                .writer("u2")
                .donateDate("20240202")
                .genderFlag("M")
                .donateAge(45)
                .writerId("u2")
                .delFlag("N")
                .donorBirthdate(LocalDate.of(1975,2,2))
                .build();
        em.persist(m1);
        em.persist(m2);

        // 한 건에 댓글 2개 등록
        MemorialReply r1 = MemorialReply.builder()
                .memorial(m1)
                .replyContents("댓글1")
                .replyWriteTime(LocalDateTime.now())
                .build();
        em.persist(r1);
        MemorialReply r2 = MemorialReply.builder()
                .memorial(m1)
                .replyContents("댓글2")
                .replyWriteTime(LocalDateTime.now())
                .build();
        em.persist(r2);

        em.flush();

        Pageable pageable = PageRequest.of(0, 10, Sort.by("donateSeq").descending());

        // when
        Page<MemorialResponseDto> page = memorialRepository.findByFilter(
                "홍", null, null, pageable);

        // then
        assertThat(page.getTotalElements()).isEqualTo(1);
        MemorialResponseDto dto = page.getContent().get(0);
        assertThat(dto.getDonorName()).isEqualTo("홍길동");
        // 댓글 2건일 때 COUNT(r.replySeq) 검증
        assertThat(dto.getCommentCount()).isEqualTo(2L);
    }
}
