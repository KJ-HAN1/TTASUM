package com.ttasum.memorial.domain.repository.memorial;

import com.ttasum.memorial.domain.entity.memorial.MemorialReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemorialReplyRepository extends JpaRepository<MemorialReply, Integer> {
    // 특정 기증자 게시글 댓글 목록 조회
    List<MemorialReply> findByMemorialDonateSeqOrderByReplyWriteTimeAsc(Integer donateSeq);
}
