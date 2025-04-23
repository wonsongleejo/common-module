package com.monkey.common_module.feign;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

// @RestController 요청 진입 시 Authorization 헤더를 ThreadLocal에 저장하는 AOP
@Aspect
@Component
@Slf4j
public class AuthorizationHeaderAspect {

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object captureAuthorizationHeader(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attr != null) {
            HttpServletRequest request = attr.getRequest();
            String token = request.getHeader("Authorization");
            if (token != null) {
                AuthorizationContextHolder.setToken(token);
                log.debug("Authorization 헤더 저장: {}", token);
            }
        }

        try {
            return joinPoint.proceed();
        } finally {
            AuthorizationContextHolder.clear();
        }
    }
}
