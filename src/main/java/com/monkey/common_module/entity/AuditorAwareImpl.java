package com.monkey.common_module.entity;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {
    private static final ThreadLocal<Long> currentAuditor = new ThreadLocal<>();

    // 컨트롤러나 서비스에서 userId 설정
    public static void setCurrentAuditor(Long userId) {
        currentAuditor.set(userId);
    }

    // 요청 끝나고 호출해서 ThreadLocal 초기화
    public static void clear() {
        currentAuditor.remove();
    }

    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(currentAuditor.get());
    }

}
