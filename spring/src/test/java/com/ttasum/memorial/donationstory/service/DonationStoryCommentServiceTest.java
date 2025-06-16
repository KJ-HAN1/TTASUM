//package com.ttasum.memorial.donationstory.service;
//
//import com.ttasum.memorial.domain.entity.DonationStory.DonationStory;
//import com.ttasum.memorial.domain.entity.DonationStory.DonationStoryComment;
//import com.ttasum.memorial.domain.repository.DonationStory.DonationStoryCommentRepository;
//import com.ttasum.memorial.domain.repository.DonationStory.DonationStoryRepository;
//import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentCreateRequestDto;
//import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentDeleteRequestDto;
//import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentResponseDto;
//import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentUpdateRequestDto;
//import com.ttasum.memorial.exception.DonationStory.DonationStoryCommentNotFoundException;
//import com.ttasum.memorial.exception.DonationStory.DonationStoryNotFoundException;
//import com.ttasum.memorial.exception.DonationStory.InvalidCommentPasscodeException;
//import com.ttasum.memorial.service.DonationStory.DonationStoryCommentService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class DonationStoryCommentServiceTest {
//
//    @Mock
//    private DonationStoryRepository storyRepository;
//
//    @Mock
//    private DonationStoryCommentRepository commentRepository;
//
//    @InjectMocks
//    private DonationStoryCommentService commentService;
//
//    @Test
//    @DisplayName("댓글 생성 성공 시 commentSeq 반환")
//    void createComment_Success() {
//        // given
//        Integer storySeq = 1;
//        DonationStoryCommentCreateRequestDto dto = new DonationStoryCommentCreateRequestDto();
//        dto.setCommentWriter("홍길동");
//        dto.setCommentPasscode("pw123");
//        dto.setContents("좋은 이야기입니다.");
//
//        DonationStory story = DonationStory.builder()
//                .areaCode("A01")
//                .title("제목")
//                .donorName("기증자")
//                .passcode("spw")
//                .writer("작성자")
//                .anonymityFlag("N")
//                .build();
//
//        when(storyRepository.findByIdAndDelFlag(storySeq, "N"))
//                .thenReturn(Optional.of(story));
//
//        DonationStoryComment saved = DonationStoryComment.builder()
//                .story(story)
//                .writer(dto.getCommentWriter())
//                .passcode(dto.getCommentPasscode())
//                .contents(dto.getContents())
//                .writerId(null)
//                .modifierId(null)
//                .build();
//
//        ReflectionTestUtils.setField(saved, "commentSeq", 42);
//
//        when(commentRepository.save(any(DonationStoryComment.class)))
//                .thenReturn(saved);
//
//        // when
//        int result = commentService.createComment(storySeq, dto);
//
//        // then
//        assertThat(result).isEqualTo(42);
//        verify(storyRepository).findByIdAndDelFlag(storySeq, "N");
//        verify(commentRepository).save(any(DonationStoryComment.class));
//    }
//
//
//    @Test
//    @DisplayName("댓글 생성 실패: 스토리 미존재 시 DonationStoryNotFoundException 발생")
//    void createComment_StoryNotFound() {
//        Integer storySeq = 999;
//        when(storyRepository.findByIdAndDelFlag(storySeq,"N")).thenReturn(Optional.empty());
//        DonationStoryCommentCreateRequestDto dto = new DonationStoryCommentCreateRequestDto();
//
//        assertThatThrownBy(() -> commentService.createComment(storySeq, dto))
//                .isInstanceOf(DonationStoryNotFoundException.class);
//
//        verify(commentRepository, never()).save(any());
//    }
//
//    @Test
//    @DisplayName("댓글 목록 조회: delFlag='N'인 댓글만 반환")
//    void getComments_Success() {
//        Integer storySeq = 1;
//        DonationStoryComment c1 = DonationStoryComment.builder()
//                .story(null)
//                .writer("A")
//                .passcode("p")
//                .contents("첫 댓글")
//                .writerId(null)
//                .modifierId(null)
//                .build();
//        DonationStoryComment c2 = DonationStoryComment.builder()
//                .story(null)
//                .writer("B")
//                .passcode("q")
//                .contents("두번째 댓글")
//                .writerId(null)
//                .modifierId(null)
//                .build();
//
//        when(commentRepository
//                .findByStory_IdAndDelFlagOrderByWriteTimeAsc(storySeq, "N"))
//                .thenReturn(Arrays.asList(c1, c2));
//
//        // when
//        List<DonationStoryCommentResponseDto> dtos = commentService.getComments(storySeq);
//
//        // then
//        assertThat(dtos).hasSize(2);
//        assertThat(dtos.get(0).getContents()).isEqualTo("첫 댓글");
//        assertThat(dtos.get(1).getWriter()).isEqualTo("B");
//    }
//
//    @Test
//    @DisplayName("댓글 수정 성공: updateIfPasscodeMatches 호출")
//    void updateComment_Success() {
//        Integer storySeq = 1, commentSeq = 10;
//        DonationStoryCommentUpdateRequestDto dto = new DonationStoryCommentUpdateRequestDto();
//        dto.setCommentPasscode("correct");
//        dto.setContents("수정된 내용");
//
//        DonationStoryComment existing = spy(DonationStoryComment.builder()
//                .story(null)
//                .writer("X")
//                .passcode("correct")
//                .contents("old")
//                .writerId(null)
//                .modifierId(null)
//                .build());
//        ReflectionTestUtils.setField(existing, "commentSeq", commentSeq);
//
//        when(commentRepository
//                .findByStory_IdAndCommentSeqAndDelFlag(storySeq, commentSeq, "N"))
//                .thenReturn(Optional.of(existing));
//
//        // when
//        commentService.updateComment(storySeq, commentSeq, dto);
//
//        // then
//        verify(existing).updateIfPasscodeMatches("correct", "수정된 내용", null);
//    }
//
//    @Test
//    @DisplayName("댓글 수정 실패: 댓글 미존재 시 DonationStoryCommentNotFoundException 발생")
//    void updateComment_NotFound() {
//        when(commentRepository
//                .findByStory_IdAndCommentSeqAndDelFlag(1, 99, "N"))
//                .thenReturn(Optional.empty());
//
//        DonationStoryCommentUpdateRequestDto dto = new DonationStoryCommentUpdateRequestDto();
//        dto.setCommentPasscode("any");
//
//        assertThatThrownBy(() ->
//                commentService.updateComment(1, 99, dto))
//                .isInstanceOf(DonationStoryCommentNotFoundException.class);
//    }
//
//    @Test
//    @DisplayName("댓글 수정 실패: 패스코드 불일치 시 InvalidCommentPasscodeException 발생")
//    void updateComment_InvalidPasscode() {
//        Integer storySeq = 1, commentSeq = 10;
//        DonationStoryCommentUpdateRequestDto dto = new DonationStoryCommentUpdateRequestDto();
//        dto.setCommentPasscode("wrong");
//        dto.setContents("irrelevant");
//
//        DonationStoryComment existing = DonationStoryComment.builder()
//                .story(null)
//                .writer("X")
//                .passcode("correct")
//                .contents("old")
//                .writerId(null)
//                .modifierId(null)
//                .build();
//        ReflectionTestUtils.setField(existing, "commentSeq", commentSeq);
//
//        when(commentRepository
//                .findByStory_IdAndCommentSeqAndDelFlag(storySeq, commentSeq, "N"))
//                .thenReturn(Optional.of(existing));
//
//        assertThatThrownBy(() ->
//                commentService.updateComment(storySeq, commentSeq, dto))
//                .isInstanceOf(InvalidCommentPasscodeException.class);
//    }
//
//    @Test
//    @DisplayName("댓글 삭제 성공: deleteIfPasscodeMatches 호출")
//    void softDeleteComment_Success() {
//        Integer storySeq = 1, commentSeq = 20;
//        DonationStoryCommentDeleteRequestDto dto = new DonationStoryCommentDeleteRequestDto();
//        dto.setCommentPasscode("ok");
//
//        DonationStoryComment existing = spy(DonationStoryComment.builder()
//                .story(null)
//                .writer("X")
//                .passcode("ok")
//                .contents("c")
//                .writerId(null)
//                .modifierId(null)
//                .build());
//        ReflectionTestUtils.setField(existing, "commentSeq", commentSeq);
//
//        when(commentRepository
//                .findByStory_IdAndCommentSeqAndDelFlag(storySeq, commentSeq, "N"))
//                .thenReturn(Optional.of(existing));
//
//        // when
//        commentService.softDeleteComment(storySeq, commentSeq, dto);
//
//        // then
//        verify(existing).deleteIfPasscodeMatches("ok", null);
//    }
//
//    @Test
//    @DisplayName("댓글 삭제 실패: 댓글 미존재 시 DonationStoryCommentNotFoundException 발생")
//    void softDeleteComment_NotFound() {
//        when(commentRepository
//                .findByStory_IdAndCommentSeqAndDelFlag(1, 99, "N"))
//                .thenReturn(Optional.empty());
//
//        DonationStoryCommentDeleteRequestDto dto = new DonationStoryCommentDeleteRequestDto();
//        dto.setCommentPasscode("any");
//
//        assertThatThrownBy(() ->
//                commentService.softDeleteComment(1, 99, dto))
//                .isInstanceOf(DonationStoryCommentNotFoundException.class);
//    }
//
//    @Test
//    @DisplayName("댓글 삭제 실패: 패스코드 불일치 시 InvalidCommentPasscodeException 발생")
//    void softDeleteComment_InvalidPasscode() {
//        Integer storySeq = 1, commentSeq = 20;
//        DonationStoryCommentDeleteRequestDto dto = new DonationStoryCommentDeleteRequestDto();
//        dto.setCommentPasscode("bad");
//
//        DonationStoryComment existing = DonationStoryComment.builder()
//                .story(null)
//                .writer("X")
//                .passcode("good")
//                .contents("c")
//                .writerId(null)
//                .modifierId(null)
//                .build();
//        ReflectionTestUtils.setField(existing, "commentSeq", commentSeq);
//
//        when(commentRepository
//                .findByStory_IdAndCommentSeqAndDelFlag(storySeq, commentSeq, "N"))
//                .thenReturn(Optional.of(existing));
//
//        assertThatThrownBy(() ->
//                commentService.softDeleteComment(storySeq, commentSeq, dto))
//                .isInstanceOf(InvalidCommentPasscodeException.class);
//    }
//}
