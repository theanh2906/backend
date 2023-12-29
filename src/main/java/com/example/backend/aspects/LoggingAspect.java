package com.example.backend.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggingAspect {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(* com.example.backend.*.*.*(..))")
    public void serviceMethods() {
    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        LOG.info("Exiting method: " + joinPoint.getSignature().getName() + " with result: " + result);
        ;
    }

    @Before("execution(* com.example.backend.*.*.*(..))")
    public void logBeforeExecuting(JoinPoint joinPoint) {
        LOG.info("Executing method: " + joinPoint.getSignature().getName());
        ;
    }

    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
        LOG.error("Exception in method: " + joinPoint.getSignature().getName() + " with exception: " + ex.getMessage());
    }
}
