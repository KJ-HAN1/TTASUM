//package com.ttasum.memorial.donationstory.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.ttasum.memorial.controller.DonationStoryCommentController;
//import com.ttasum.memorial.dto.DonationStory.DonationStoryUpdateRequestDto;
//import com.ttasum.memorial.dto.DonationStoryComment.DonationStoryCommentCreateRequestDto;
//import com.ttasum.memorial.service.DonationStory.DonationStoryCommentService;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.BDDMockito.given;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(DonationStoryCommentController.class)
//@AutoConfigureMockMvc
//public class DonationStoryCommentControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private DonationStoryCommentService commentService;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    @DisplayName("댓글 생성 성공 시 201 Created와 Location 헤더 반환")
//    void createComment_success() throws Exception {
//        // given
//        Integer storySeq = 1;
//        Integer commentSeq = 10;
//
//        DonationStoryCommentCreateRequestDto dto = new DonationStoryCommentCreateRequestDto();
//        dto.setCommentWriter("테스트 코디");
//        dto.setCommentPasscode("123456");
//        dto.setContents("테스트 내용");
//
//        // void 반환 -> doNothing
//        given(commentService.createComment(eq(storySeq), any())).willReturn(commentSeq);
//
//        mockMvc.perform(post("/donationLetters/{storySeq}/comments", storySeq)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dto))) // DTO → JSON 문자열로 변환
//                .andExpect(status().isCreated())
//                .andExpect(header().string("Location", String.format("/donationLetters/%d/comments/%d", storySeq, commentSeq)));
//    }
//
//    @Nested
//    @DisplayName("댓글 생성 실패 테스트")
//    class CreateCommentFailTests {
//
//        @Test
//        @DisplayName("작성자 미입력 시 400")
//        void createComment_fail_noWriter() throws Exception {
//            DonationStoryCommentCreateRequestDto dto = new DonationStoryCommentCreateRequestDto();
//            dto.setCommentWriter(""); // 유효하지 않음
//            dto.setCommentPasscode("abcd1234");
//            dto.setContents("댓글 내용");
//
//            mockMvc.perform(post("/donationLetters/1/comments")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(dto)))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("비밀번호 미입력 시 400")
//        void createComment_fail_noPasscode() throws Exception {
//            DonationStoryCommentCreateRequestDto dto = new DonationStoryCommentCreateRequestDto();
//            dto.setCommentWriter("홍길동");
//            dto.setCommentPasscode(""); // 유효하지 않음
//            dto.setContents("댓글 내용");
//
//            mockMvc.perform(post("/donationLetters/1/comments")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(dto)))
//                    .andExpect(status().isBadRequest());
//        }
//
//        @Test
//        @DisplayName("내용 미입력 시 400")
//        void createComment_fail_noContents() throws Exception {
//            DonationStoryCommentCreateRequestDto dto = new DonationStoryCommentCreateRequestDto();
//            dto.setCommentWriter("홍길동");
//            dto.setCommentPasscode("abcd1234");
//            dto.setContents(""); // 유효하지 않음
//
//            mockMvc.perform(post("/donationLetters/1/comments")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(dto)))
//                    .andExpect(status().isBadRequest());
//        }
//
//    }
//
//    @Nested
//    @DisplayName("댓글 수정 테스트")
//    class UpdateCommentTests {
//
//        @Test
//        @DisplayName("정상 수정 시 200 OK")
//        void updateComment_success() throws Exception {
//            DonationStoryUpdateRequestDto dto = new DonationStoryUpdateRequestDto();
//            dto.setStoryPasscode("123456");
//            dto.setStoryContents("수정된 댓글");
//
//
//
//        }
//    }
//
//}
