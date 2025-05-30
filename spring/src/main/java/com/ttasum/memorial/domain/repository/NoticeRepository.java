package com.ttasum.memorial.domain.repository;

import com.ttasum.memorial.domain.entity.ArticleId;
import com.ttasum.memorial.domain.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, ArticleId> {

    Page<Notice> findByArticleIdBoardCodeAndDelFlag(
            String boardCode,
            String delFlag,
            Pageable pageable
    );
}