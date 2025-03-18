package com.example.onlineshoppingmall.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PerformanceAspect {
    // 메서드 실행 시간이 이 값보다 오래 걸리면 경고 로그 출력 (1초)
    private static final long THRESHOLD_MS = 1000;

    @Around("execution(* com.example.onlineshoppingmall.domain.*.service.*.*(..))")
    public Object logPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();

        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;

            if (executionTime > THRESHOLD_MS) {
                String className = joinPoint.getTarget().getClass().getSimpleName();
                String methodName = joinPoint.getSignature().getName();

                log.warn("⚠️ Performance: {}.{} took {}ms to execute",
                        className, methodName, executionTime);
            }
        }
    }
}
