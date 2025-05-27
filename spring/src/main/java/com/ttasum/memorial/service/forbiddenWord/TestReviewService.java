// 임시로 사용
package com.ttasum.memorial.service.forbiddenWord;

import com.ttasum.memorial.annotation.blameText.CheckBlameText;
import com.ttasum.memorial.annotation.forbiddenWord.CheckForbiddenWord;
import com.ttasum.memorial.dto.forbiddenWord.ReviewRequestDto;
import com.ttasum.memorial.exception.blameText.BlamTextException;
import com.ttasum.memorial.exception.forbiddenWord.ForbiddenWordException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
public class TestReviewService {

    @CheckBlameText
//    @Transactional()
    public ResponseEntity<Map<String, Object>> saveReview(ReviewRequestDto request) throws BlamTextException {

        // 응답 json
        Map<String, Object> result = new HashMap<>();
        result.put("status", "success");
        result.put("message", "리뷰가 정상적으로 저장되었습니다.");
        // DB에 저장

        return ResponseEntity.ok(result);
    }
}
