// 임시로 사용
package com.ttasum.memorial.service.forbiddenWord;

import com.ttasum.memorial.annotation.blameText.CheckBlameText;
import com.ttasum.memorial.annotation.forbiddenWord.CheckForbiddenWord;
import com.ttasum.memorial.dto.blameText.ResponseDto;
import com.ttasum.memorial.dto.forbiddenWord.ReviewRequestDto;
import com.ttasum.memorial.exception.blameText.BlamTextException;
import com.ttasum.memorial.exception.forbiddenWord.ForbiddenWordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
public class TestReviewService {

    @CheckBlameText  // 이 메서드가 AOP의 Pointcut 대상임을 나타냄
    public ResponseEntity<?> saveReview(ReviewRequestDto request){
        // 이 메서드 자체가 JoinPoint이며, 실행 전후에 Advice가 적용됨
        return ResponseEntity.ok(ResponseDto.ok("success", HttpStatus.OK.value(),
                "등록이 성공적으로 완료되었습니다."));
    }
}
