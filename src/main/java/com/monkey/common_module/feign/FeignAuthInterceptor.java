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
        try {
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            if (attr == null) {
                log.warn("FeignAuthInterceptor: RequestAttributes가 null입니다.");
                return;
            }

            HttpServletRequest request = attr.getRequest();
            String token = request.getHeader("Authorization");

            if (token != null && !token.isBlank()) {
                template.header("Authorization", token);
                log.debug("Feign 요청에 Authorization 헤더 추가 완료");
            } else {
                log.warn("Feign 요청 시 Authorization 헤더 없음");
            }

        } catch (Exception e) {
            log.error("FeignAuthInterceptor 에러", e);
        }
    }
}
