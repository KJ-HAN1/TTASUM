package com.ttasum.memorial.domain.repository.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;

// 공지사항 커스텀 리포지토리 구현체
@Repository
@RequiredArgsConstructor
public class NoticeRepositoryImpl implements NoticeRepositoryCustom {

    private final EntityManager em;

    /**
     * 조회수 증가 쿼리 실행
     * - 성능 최적화를 위해 DB에서 직접 증가 처리함
     * - 서비스 단에서 엔티티의 increaseReadCount() 대신 사용됨
     */
    @Override
    @Transactional
    public void increaseHitCount(String boardCode, Integer articleSeq) {
        em.createQuery("""
                update Notice n
                set n.readCount = n.readCount + 1
                where n.id.boardCode = :boardCode and n.id.articleSeq = :articleSeq
            """)
                .setParameter("boardCode", boardCode)
                .setParameter("articleSeq", articleSeq)
                .executeUpdate();
    }

    // TODO: QueryDSL 혹은 Criteria API를 활용한 searchNotices 구현
    // public Page<Notice> searchNotices(...) { ... }
}
