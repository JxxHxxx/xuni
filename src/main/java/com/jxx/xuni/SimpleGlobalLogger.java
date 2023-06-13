package com.jxx.xuni;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class SimpleGlobalLogger {

    @Around("execution(* com.jxx.xuni..*Controller.*(..))")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Signature signature = joinPoint.getSignature();
        Object result = joinPoint.proceed();

        long end = System.currentTimeMillis();

        long workingTime = end - start;
        log.info("[Class Info : {}][REQUEST METHOD : {}] [TIME : {}ms]", signature.getDeclaringTypeName(), signature.getName(), workingTime);

        return result;
    }

}
