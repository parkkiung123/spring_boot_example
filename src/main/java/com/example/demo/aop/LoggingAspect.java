package com.example.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.example.demo.services.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().getName();
        logger.info("[AOP] 메서드 실행 전: " + method);
    }
}
