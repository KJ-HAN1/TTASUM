package com.ttasum.memorial.domain.entity.DonationStory;

import javax.persistence.*;

// 기증후 스토리 댓글 엔티티
@Entity
@Table(name = "tb25_421_donation_story_comment")
public class DonationStoryComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_seq")
    private Integer commentSeq;

    @Column(name = "story_seq", nullable = false)
    private Integer storySeq;
}
