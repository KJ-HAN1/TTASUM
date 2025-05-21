package com.ttasum.memorial.domain.repository.DonationStory;

import com.ttasum.memorial.domain.entity.DonationStory.DonationStoryComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// 기증후 스토리 댓글 JPA 리포지터리
@Repository
public interface DonationStoryCommentRepository extends JpaRepository<DonationStoryComment, Integer> {

}
