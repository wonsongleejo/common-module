package com.monkey.common_module.aop;

/**
 * @author chobeebee
 * @version 1
 * @value
 * ALL: 모든 접근이 가능
 * USER: 회원만 접근 가능
 * MASTER: 마스터만 접근 가능
 * MANAGER: 스토어 매니저만 접근 가능
 * MANAGER_OR_MASTER: 매니저 및 마스터만 접근 가능
 *
 */
public enum AccessLevel {
    ALL,
    USER,
    MASTER,
    MANAGER,
    MANAGER_OR_MASTER
}
