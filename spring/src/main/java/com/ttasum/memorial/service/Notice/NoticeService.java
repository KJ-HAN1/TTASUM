package com.ttasum.memorial.service.notice;

import com.ttasum.memorial.domain.entity.notice.NoticeId;
import com.ttasum.memorial.domain.entity.notice.Notice;
import com.ttasum.memorial.domain.repository.notice.NoticeRepository;
import com.ttasum.memorial.dto.notice.response.NoticeResponseDto;
import com.ttasum.memorial.exception.notice.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private static final String BOARD_CODE = "7";  // 게시판 코드(공지사항)
    private static final String ACTIVE_FLAG = "N";       // 삭제되지 않음

    private final NoticeRepository noticeRepository;

    /**
     * 페이징된 공지사항 목록 조회
     * @param pageable 페이징 처리용 파라미터 객체
     * @return Entity → DTO 변환
     */
    @Transactional(readOnly = true)
    public Page<NoticeResponseDto> getAllNotices(Pageable pageable) {
        return noticeRepository
                .findByArticleIdBoardCodeAndDelFlag(BOARD_CODE, ACTIVE_FLAG, pageable)
                .map(NoticeResponseDto::fromEntity);
    }

    /**
     * 단건 공지사항 조회
     * @param articleSeq 게시글 번호
     * @return 응답 dto 반환, 없으면 404 반환
     */
    @Transactional
    public NoticeResponseDto getNoticeById(Integer articleSeq) {
        NoticeId id = new NoticeId(BOARD_CODE, articleSeq);
        Notice notice = noticeRepository.findById(id)
                .filter(n -> ACTIVE_FLAG.equals(n.getDelFlag()))
                .orElseThrow(() ->
                        new ResourceNotFoundException("공지사항을 찾을 수 없습니다. id=" + articleSeq)
                );
        // 조회수 증가
        notice.increaseReadCount();
        return NoticeResponseDto.fromEntity(notice);
    }

}
