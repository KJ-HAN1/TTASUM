package com.ttasum.memorial.donationstory.service;

import com.ttasum.memorial.domain.entity.DonationStory.DonationStory;
import com.ttasum.memorial.domain.entity.DonationStory.DonationStoryComment;
import com.ttasum.memorial.domain.repository.DonationStory.DonationStoryCommentRepository;
import com.ttasum.memorial.domain.repository.DonationStory.DonationStoryRepository;
import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentCreateRequestDto;
import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentDeleteRequestDto;
import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentResponseDto;
import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentUpdateRequestDto;
import com.ttasum.memorial.exception.DonationStory.DonationStoryCommentNotFoundException;
import com.ttasum.memorial.exception.DonationStory.DonationStoryNotFoundException;
import com.ttasum.memorial.service.DonationStoryCommentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DonationStoryCommentServiceTest {

    @InjectMocks
    private DonationStoryCommentService commentService;

    @Mock
    private DonationStoryRepository storyRepository;

    @Mock
    private DonationStoryCommentRepository commentRepository;

    private DonationStory story;
    private DonationStoryComment comment;

    @BeforeEach
    void setUp() {
        story = mock(DonationStory.class);
        comment = DonationStoryComment.builder()
                .story(story)
                .writer("홍길동")
                .passcode("pw1234")
                .contents("댓글 내용입니다")
                .writerId(null)
                .modifierId(null)
                .build();
        ReflectionTestUtils.setField(comment, "commentSeq", 1); // ID 수동 설정
    }

    @Test
    void getComments_shouldReturnDtos_withCorrectFields() {
        // given
        when(commentRepository.findByStory_StorySeqAndDelFlagOrderByWriteTimeAsc(1, "N"))
                .thenReturn(List.of(comment));

        // when
        List<DonationStoryCommentResponseDto> results = commentService.getComments(1);

        // then
        assertThat(results).hasSize(1);

        DonationStoryCommentResponseDto dto = results.get(0);
        assertThat(dto.getId()).isEqualTo(comment.getCommentSeq());
        assertThat(dto.getWriter()).isEqualTo(comment.getWriter());
        assertThat(dto.getContents()).isEqualTo(comment.getContents());
        assertThat(dto.getWriteTime()).isEqualTo(comment.getWriteTime());

        verify(commentRepository, times(1))
                .findByStory_StorySeqAndDelFlagOrderByWriteTimeAsc(1, "N");
    }


    @Test
    void updateComment_shouldUpdate_whenPasswordMatches() {
        // given
        DonationStoryCommentUpdateRequestDto dto = new DonationStoryCommentUpdateRequestDto();
        dto.setPasscode("pw1234");
        dto.setContents("수정된 내용");

        when(commentRepository.findByIdAndPasscodeAndDelFlag(1, "pw1234", "N"))
                .thenReturn(Optional.of(comment));

        // when
        commentService.updateComment(1, dto);

        // then
        assertThat(comment.getContents()).isEqualTo("수정된 내용");
    }

    @Test
    void updateComment_shouldThrow_whenPasswordIncorrect() {
        // given
        DonationStoryCommentUpdateRequestDto dto = new DonationStoryCommentUpdateRequestDto();
        dto.setPasscode("wrong");
        dto.setContents("수정 실패");

        when(commentRepository.findByIdAndPasscodeAndDelFlag(1, "wrong", "N"))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> commentService.updateComment(1, dto))
                .isInstanceOf(DonationStoryCommentNotFoundException.class);
    }

    @Test
    void softDeleteComment_shouldMarkAsDeleted_whenPasswordMatches() {
        // given
        DonationStoryCommentDeleteRequestDto dto = new DonationStoryCommentDeleteRequestDto();
        dto.setPasscode("pw1234");

        when(commentRepository.findByIdAndPasscodeAndDelFlag(1, "pw1234", "N"))
                .thenReturn(Optional.of(comment));

        // when
        commentService.softDeleteComment(1, dto);

        // then
        assertThat(comment.getDelFlag()).isEqualTo("Y");
    }

    @Test
    void softDeleteComment_shouldThrow_whenPasswordIncorrect() {
        // given
        DonationStoryCommentDeleteRequestDto dto = new DonationStoryCommentDeleteRequestDto();
        dto.setPasscode("wrong");

        when(commentRepository.findByIdAndPasscodeAndDelFlag(1, "wrong", "N"))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> commentService.softDeleteComment(1, dto))
                .isInstanceOf(DonationStoryCommentNotFoundException.class);
    }

}
