package com.monkey.common_module.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

// Feign 요청 시 현재 HttpServletRequest의 Authorization 헤더를 복사해서 전달하는 인터셉터
@Configuration
@Slf4j
public class FeignAuthInterceptor implements RequestInterceptor {

    @PostConstruct
    public void init() {
        log.info("FeignAuthInterceptor Bean 등록됨");
    }

    @Override
    public void apply(RequestTemplate template) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            log.warn("RequestAttributes가 null입니다.");
            return;
        }

        HttpServletRequest request = attrs.getRequest();

        // JWT
        String token = AuthorizationContextHolder.getToken();
        if (token != null && !token.isBlank()) {
            template.header("Authorization", token);
            log.info("🧩 Authorization 헤더 추가 완료");
        }

        // 커스텀 헤더 추가
        String userId = request.getHeader("X-User-Id");
        String username = request.getHeader("X-User-Name");
        String role = request.getHeader("X-User-Role");

        if (userId != null) {
            template.header("X-User-Id", userId);
        }
        if (username != null) {
            template.header("X-User-Name", username);
        }
        if (role != null) {
            template.header("X-User-Role", role);
        }

        log.info("🧩 Feign 요청에 X-User 헤더들 추가: id={}, name={}, role={}", userId, username, role);
    }
}
