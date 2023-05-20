package com.jxx.xuni.common.aop;

import com.jxx.xuni.auth.application.MemberDetails;
import com.jxx.xuni.common.exception.NotPermissionException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import static com.jxx.xuni.common.exception.CommonExceptionMessage.FORBIDDEN;

/**
 * 본인 인증을 위한 Aspect
 * 적용 위치 Suffix Controller.class Suffix Self method
 * 메서드 내 첫 번째 인자는 Long type의 식별자를 인자로 받는다.
 * 두번째 인자는 MemberDetails 구현체를 받는다.
 */

@Aspect
@Component
public class IdentificationAspect {

    @Around("execution(* com.jxx.xuni..*Controller.*Self(..))")
    public Object identificateRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        Long identifier = castToLong(args[0]);
        MemberDetails memberDetails = castToMemberDetail(args[1]);

        if (identifier != memberDetails.getUserId()) {
            throw new NotPermissionException(FORBIDDEN);
        }

        return joinPoint.proceed();
    }

    private MemberDetails castToMemberDetail(Object md) {
        try {
            return (MemberDetails) md;
        } catch (Exception e) {
            throw new ClassCastException();
        }
    }

    private Long castToLong(Object id) {
        try {
            return (Long) id;
        } catch (Exception e) {
            throw new ClassCastException();
        }
    }
}
