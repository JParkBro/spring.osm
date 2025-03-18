package com.example.onlineshoppingmall.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TransactionLoggingAspect {
    @Before("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void beforeTransaction(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.debug("🔄 Transaction started: {}.{}", className, methodName);
    }

    @AfterReturning("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void afterTransaction(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.debug("✅ Transaction committed: {}.{}", className, methodName);
    }

    @AfterThrowing(pointcut = "@annotation(org.springframework.transaction.annotation.Transactional)", throwing = "ex")
    public void afterTransactionThrowsException(JoinPoint joinPoint, Exception ex) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.error("❌ Transaction rolled back: {}.{} - Error: {}",
                className, methodName, ex.getMessage());
    }
}
