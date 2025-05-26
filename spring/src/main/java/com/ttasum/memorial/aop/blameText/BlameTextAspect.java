package com.ttasum.memorial.aop.blameText;

import com.ttasum.memorial.dto.forbiddenWord.ReviewRequestDto;
import com.ttasum.memorial.exception.blameText.BlamTextException;
import com.ttasum.memorial.service.blameText.BlameTextCheckerService;
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

    @Around("@annotation(com.ttasum.memorial.annotation.blameText.CheckBlameText)")
    public Object checkBlameText(ProceedingJoinPoint joinPoint) throws Throwable {
        for (Object arg : joinPoint.getArgs()) {
            if (arg instanceof ReviewRequestDto req) {
                if(checkerService.checkBlameText(req.getSentence()) == 1){
                    throw new BlamTextException("비난하는 의도가 예상 되는 글 입니다.");
                }
            }
        }
        return joinPoint.proceed();
    }
}
