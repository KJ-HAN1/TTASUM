package com.ttasum.memorial.service.blameText;

import com.ttasum.memorial.domain.entity.Comment;
import com.ttasum.memorial.domain.entity.Contents;
import com.ttasum.memorial.domain.entity.donationStory.DonationStory;
import com.ttasum.memorial.domain.entity.donationStory.DonationStoryComment;
import com.ttasum.memorial.domain.entity.Story;
import com.ttasum.memorial.domain.entity.blameText.*;
import com.ttasum.memorial.domain.repository.donationStory.DonationStoryCommentRepository;
import com.ttasum.memorial.domain.repository.blameText.BlameTextCommentRepository;
import com.ttasum.memorial.domain.repository.blameText.BlameTextLetterRepository;
import com.ttasum.memorial.domain.repository.blameText.BlameTextLetterSentenceRepository;
import com.ttasum.memorial.dto.blameText.BlameResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

@Service
@AllArgsConstructor
public class BlameTextPersistenceService {
    private final BlameTextLetterRepository blameTextLetterRepository;
    private final BlameTextLetterSentenceRepository blameTextLetterSentenceRepository;
    private final BlameTextCommentRepository blameTextCommentRepository;
    private final DonationStoryCommentRepository donationStoryCommentRepository;

    // 매직 넘버 제거
    private static final double DEFAULT_REGULATION = 0.7;
    private static final String DEFAULT_BOARD_TYPE = "donation";

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public <T extends Contents> void saveToDb(BlameResponseDto response, T contents) {
        if (contents instanceof Story) {
            if (contents instanceof DonationStory story) {  //패턴 변수
                BlameTextLetter savedLetter = blameTextLetterRepository.save(
                        setBlameLetter(response, story));
                setBlameLetterSentence(response, savedLetter);
            }
        } else if (contents instanceof Comment) {
            if(contents instanceof DonationStoryComment comment) {
                BlameTextComment savedComment = blameTextCommentRepository.save(
                        setBlameComment(response, comment));
                this.setBlameLetterSentence(response, savedComment);
            }
        } else {
            throw new IllegalArgumentException("지원하지 않는 타입입니다: " + contents.getClass().getSimpleName());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)  //호출자가 트랜잭션 중이더라도 기존 트랜잭션을 분리
    public void updateToDb(BlameResponseDto response, Contents contents) {
        if(contents instanceof Story) {
            if(contents instanceof DonationStory story) {
                BlameTextLetter existing =
                        blameTextLetterRepository.findByDonationStory_IdAndDeleteFlag(story.getId(), 0)
                                .orElseThrow(() -> new EntityNotFoundException("기존 비난 텍스트 없음"));
                // 기존 데이터 삭제 delete_flag (text, sentence 모두)
                existing.setDeleteFlag(1);
                existing.setUpdateTime(LocalDateTime.now());
                blameTextLetterRepository.save(existing);
                // 다시 저장
                this.saveToDb(response, story);
            }
        } else if(contents instanceof Comment) {
            if(contents instanceof DonationStoryComment comment) {
                BlameTextComment existing =
                        blameTextCommentRepository.findByComment_CommentSeqAndDeleteFlag(comment.getCommentSeq(), 0)
                                .orElseThrow(() -> new EntityNotFoundException("기존 비난 텍스트 없음"));
                // 기존 데이터 삭제 delete_flag (text, sentence 모두)
                existing.setDeleteFlag(1);
                existing.setUpdateTime(LocalDateTime.now());
                blameTextCommentRepository.save(existing);
                // 다시 저장
                this.saveToDb(response, comment);
            }
        }

    }

    private BlameTextLetter setBlameLetter(BlameResponseDto response, DonationStory story) {
        return BlameTextLetter.builder()
                .sentence(response.getSentence())
                .blameLabel(response.getLabel())
                .blameConfidence(response.getConfidence())
                .sentenceLine(response.getDetails().size())
                .regulation(DEFAULT_REGULATION)
                .updateTime(LocalDateTime.now())
                .boardType(DEFAULT_BOARD_TYPE)
                .donationStory(story)
                .deleteFlag(0)
                .build();
    }

    private BlameTextComment setBlameComment(BlameResponseDto response, DonationStoryComment comment) {
        // 1. DonationStoryComment 저장 (영속 상태로 만들어야 함)
        donationStoryCommentRepository.save(comment);

        // 2. 이후 BlameTextComment에 연결
        return BlameTextComment.builder()
                .storySeq(comment.getStory().getId())
                .contents(response.getSentence())
                .blameLabel(response.getLabel())
                .blameConfidence(response.getConfidence())
                .sentenceLine(response.getDetails().size())
                .regulation(DEFAULT_REGULATION)
                .updateTime(LocalDateTime.now())
                .boardType(DEFAULT_BOARD_TYPE)
                .comment(comment)
                .deleteFlag(0)
                .build();
    }

    // python 서버 json 응답 값 중 Text를 sentence로 split한 결과를 DB에 저장
    private void setBlameLetterSentence(
            BlameResponseDto response, Contents savedContents){
        int seq = 0;
        if(savedContents instanceof BlameTextLetter saved) {
            ArrayList<BlameTextLetterSentence> list= new ArrayList<>();
            for (Map<String, Object> oneDetail : response.getDetails() ) {
                BlameTextLetterSentence blameTextLetterSentence = BlameTextLetterSentence.builder()
                        .id(new BlameTextLetterSentenceId(saved.getSeq(), seq++))
                        .sentence((String) oneDetail.get("sentence"))
                        .confidence((double) oneDetail.get("confidence"))
                        .label((int) oneDetail.get("label"))
                        .letter((BlameTextLetter) savedContents)
                        .build();
                list.add(blameTextLetterSentence);
            }
            saved.setSentences(list);  //부모만 save()해도 자식들이 자동 저장
        } else if (savedContents instanceof BlameTextComment saved) {
            ArrayList<BlameTextCommentSentence> list= new ArrayList<>();
            for (Map<String, Object> oneDetail : response.getDetails() ) {
                BlameTextCommentSentence blameTextCommentSentence = BlameTextCommentSentence.builder()
                        .id(new BlameTextCommentSentenceId(saved.getSeq() ,seq++))
                        .sentence((String) oneDetail.get("sentence"))
                        .confidence((double) oneDetail.get("confidence"))
                        .label((int) oneDetail.get("label"))
                        .comment((BlameTextComment) savedContents)
                        .build();
                list.add(blameTextCommentSentence);
            }
            saved.setComments(list);  //부모만 save()해도 자식들이 자동 저장
        }
    }
}
