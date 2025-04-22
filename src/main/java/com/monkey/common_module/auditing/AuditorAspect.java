package com.monkey.common_module.auditing;

import com.monkey.common_module.entity.AuditorAwareImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//모든 @RestController 내의 메서드에서 @RequestHeader("X-User-Id") Long userId 를 자동 감지,
// ThreadLocal 기반 AuditorAware에 자동으로 주입하는 AOP
@Aspect
@Component
public class AuditorAspect {

    // @RestController 내부 메서드만 타겟팅
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {}

    // Around advice: userId를 ThreadLocal에 넣고, 메서드 실행 후 clear 해줌
    @Around("restControllerMethods() && args(.., userId)")
    public Object setAuditorAround(ProceedingJoinPoint joinPoint, Long userId) throws Throwable {
        AuditorAwareImpl.setCurrentAuditor(userId);
        try {
            return joinPoint.proceed();
        } finally {
            AuditorAwareImpl.clear();
        }
    }
}
