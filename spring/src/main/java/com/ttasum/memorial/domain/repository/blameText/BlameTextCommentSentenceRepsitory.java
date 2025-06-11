package com.ttasum.memorial.domain.repository.blameText;

import com.ttasum.memorial.domain.entity.blameText.BlameTextComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlameTextCommentSentenceRepsitory extends JpaRepository<BlameTextComment, Integer> {
}
