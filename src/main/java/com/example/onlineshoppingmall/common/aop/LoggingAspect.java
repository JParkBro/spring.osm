package com.example.onlineshoppingmall.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("within(com.example.onlineshoppingmall.api..*) || within(com.example.onlineshoppingmall.web..*)")
    public void controllerPointcut() {}

    @Pointcut("within(com.example.onlineshoppingmall.domain.*.service..*)")
    public void servicePointcut() {}

    @Around("controllerPointcut()")
    public Object logAroundController(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint, "Controller");
    }

    @Around("servicePointcut()")
    public Object logAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
        return logMethod(joinPoint, "Service");
    }

    private Object logMethod(ProceedingJoinPoint joinPoint, String methodType) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.info(">>> {}: {}.{} - Started", methodType, className, methodName);

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long endTime = System.currentTimeMillis();

            log.info("<<< {}: {}.{} - Completed in {}ms", methodType, className, methodName, (endTime - startTime));

            return result;
        } catch (Exception e) {
            log.error("!!! {}: {}.{} - Error: {}", methodType, className, methodName, e.getMessage());
            throw e;
        }
    }
}
