/**
 * logging aop ( 컨트롤러에서 log 분리)
 * 현재 컨트롤러에 들어오는 모든 매서드 요청에 대해 before로 로그 남기기.
 * 로그는 요청 url , method type, ip, content 4가지로 우선 남기기.
 */
package com.ttasum.memorial.aop.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {

    @Pointcut("execution(public * com.ttasum.memorial.controller..*(..))")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void logRequest(JoinPoint joinPoint) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        MDC.put("url_name", request.getRequestURI());   // 요청 들어온 uri
        MDC.put("crud_code", request.getMethod());      // 메서드 타입(get,post,...)
        MDC.put("ip_addr", request.getRemoteAddr());   // client ip addr -> 프록시나 로드밸런서 사용하게 되면 변경 해야함

        String logText = String.format("method=%s, uri=%s, ip=%s", request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
        MDC.put("log_text", logText);

        log.info(logText);
    }

    @After("controllerMethods()")
    public void clearMDC() {
        MDC.clear(); // Thread 재사용 방지
    }

}
