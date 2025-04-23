package com.monkey.common_module.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chobeebee
 * @version 1
 * Method Role별 접근 관리 편의성을 위한 annotation
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckUserRole {
    AccessLevel value() default AccessLevel.ALL;
}
