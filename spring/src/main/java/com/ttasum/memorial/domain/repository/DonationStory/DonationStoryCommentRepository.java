package com.ttasum.memorial.domain.repository.DonationStory;

import com.ttasum.memorial.domain.entity.DonationStory.DonationStoryComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// 기증후 스토리 댓글 리포지터리
public interface DonationStoryCommentRepository extends JpaRepository<DonationStoryComment, Integer> {

    // 특정 스토리에 대한 댓글 목록 조회 (삭제되지 않은 것만)
    List<DonationStoryComment> findByStory_IdAndDelFlagOrderByWriteTimeAsc(Integer storySeq, String delFlag);

    // 댓글 ID와 비밀번호 일치 여부 확인
    Optional<DonationStoryComment> findByStory_IdAndCommentSeqAndDelFlag(Integer storySeq, Integer commentSeq, String delFlag);

}