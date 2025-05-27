package com.ttasum.memorial.aop.blameText;

import com.ttasum.memorial.dto.blameText.BlameResponseDto;
import com.ttasum.memorial.dto.forbiddenWord.ReviewRequestDto;
import com.ttasum.memorial.exception.blameText.BlamTextException;
import com.ttasum.memorial.service.blameText.BlameTextCheckerService;
import com.ttasum.memorial.service.forbiddenWord.ForbiddenWordCheckerService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class BlameTextAspect {

    @Autowired
    private final BlameTextCheckerService checkerService;
    @Autowired
    private final ForbiddenWordCheckerService forbiddenWordCheckerService;

    @Around("@annotation(com.ttasum.memorial.annotation.blameText.CheckBlameText)")
    public Object checkBlameText(ProceedingJoinPoint joinPoint) throws Throwable {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof ReviewRequestDto req) {
                BlameResponseDto response = checkerService.analyzeAndSave(req.getSentence());

                if(response.getLabel() == 1){
//                    forbiddenWordCheckerService.containsForbiddenWord(req.getSentence());
                    throw new BlamTextException("비난하는 의도가 예상 되는 글 입니다.");
                }
            }
        }
        return joinPoint.proceed();
    }
}
