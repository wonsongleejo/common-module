package com.monkey.common_module.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

// Feign 요청 시 현재 HttpServletRequest의 Authorization 헤더를 복사해서 전달하는 인터셉터
@Configuration
@Slf4j
public class FeignAuthInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String token = AuthorizationContextHolder.getToken();

        if (token != null && !token.isBlank()) {
            template.header("Authorization", token);
            log.debug("Feign 요청에 Authorization 헤더 자동 추가됨");
        } else {
            log.warn("Feign 요청 시 Authorization 헤더 없음");
        }
    }
}
