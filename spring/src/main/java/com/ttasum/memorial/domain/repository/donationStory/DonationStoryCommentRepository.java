package com.ttasum.memorial.domain.repository.donationStory;

import com.ttasum.memorial.domain.entity.donationStory.DonationStoryComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// 기증후 스토리 댓글 리포지터리
public interface DonationStoryCommentRepository extends JpaRepository<DonationStoryComment, Integer> {

    // 특정 스토리에 대한 댓글 목록 조회 (삭제되지 않은 것만)
    List<DonationStoryComment> findByStory_IdAndDelFlagOrderByWriteTimeAsc(Integer storySeq, String delFlag);

    // 댓글 ID와 비밀번호 일치 여부 확인
    Optional<DonationStoryComment> findByStory_IdAndCommentSeqAndDelFlag(Integer storySeq, Integer commentSeq, String delFlag);

    @Query("""
                SELECT c.story.id AS storyId, COUNT(c) AS count
                FROM DonationStoryComment c
                WHERE c.story.id IN :storyIds AND c.delFlag = 'N'
                GROUP BY c.story.id
            """)
    List<CommentCount> countCommentsByStoryIds(@Param("storyIds") List<Integer> storyIds);
}