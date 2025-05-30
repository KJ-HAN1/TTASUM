package com.ttasum.memorial.controller;

import com.ttasum.memorial.dto.NoticeResponseDto;
import com.ttasum.memorial.service.Notice.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/notices")
public class NoticeController {

    private final NoticeService noticeService;

    /**
     * 페이징된 공지사항 목록 반환
     * @param pageable 페이징 처리용 파라미터 객체
     */
    @GetMapping
    public Page<NoticeResponseDto> listNotices(@PageableDefault(
            // 기본 정렬·페이징 설정
            size = 10,
            sort = "writeTime",
            direction = Sort.Direction.DESC
    ) Pageable pageable) {
        return noticeService.getAllNotices(pageable);
    }

    @GetMapping("/{articleSeq}")
    public ResponseEntity<NoticeResponseDto> getNotice(@PathVariable Integer articleSeq) {
        NoticeResponseDto dto = noticeService.getNoticeById(articleSeq);
        return ResponseEntity.ok(dto);
    }

}
