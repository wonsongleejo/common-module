package com.monkey.common_module.lock;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DistributedLock {
    String key(); // 락 키
    long waitTime() default 5L; // 락 대기 시간 (초)
    long leaseTime() default 5L; // 락 점유 시간 (초)
}
