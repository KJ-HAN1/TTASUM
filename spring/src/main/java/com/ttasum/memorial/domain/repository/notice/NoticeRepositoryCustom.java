package com.ttasum.memorial.domain.repository.notice;

public interface NoticeRepositoryCustom {

    // 조회수 증가
    void increaseHitCount(String boardCode, Integer articleSeq);

    // 페이징 + 검색용 동적 쿼리 메서드
    // Page<Notice> searchNotices(List<String> boardCodes, String keyword, Pageable pageable);
}
