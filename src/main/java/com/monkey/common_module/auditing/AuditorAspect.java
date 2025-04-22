package com.monkey.common_module.auditing;

import com.monkey.common_module.entity.AuditorAwareImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

//모든 @RestController 내의 메서드에서 @RequestHeader("X-User-Id") Long userId 를 자동 감지,
// ThreadLocal 기반 AuditorAware에 자동으로 주입하는 AOP
@Aspect
@Component
@Slf4j
public class AuditorAspect {

    // @RestController 내부 메서드만 타겟팅
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void restControllerMethods() {}

    // Around advice: userId를 ThreadLocal에 넣고, 메서드 실행 후 clear 해줌
    @Around("restControllerMethods()")
    public Object setAuditorAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpServletRequest request = attr.getRequest();
            String userIdHeader = request.getHeader("X-User-Id");

            if (userIdHeader != null) {
                try {
                    Long userId = Long.parseLong(userIdHeader);
                    log.debug("AOP: X-User-Id = {}", userId);
                    AuditorAwareImpl.setCurrentAuditor(userId);
                } catch (NumberFormatException e) {
                    log.warn("AOP: X-User-Id 헤더 파싱 실패 → {}", userIdHeader);
                }
            } else {
                log.warn("AOP: X-User-Id 헤더 없음");
            }
        }

        try {
            return joinPoint.proceed();
        } finally {
            AuditorAwareImpl.clear();
        }
    }
}
