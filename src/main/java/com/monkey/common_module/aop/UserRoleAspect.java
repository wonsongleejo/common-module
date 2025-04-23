package com.monkey.common_module.aop;

import com.monkey.common_module.dto.ResponseCode;
import com.monkey.common_module.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author chobeebee
 * @version 1
 *
 * HttpServletRequest 에서 Header Value ("X-User-Role")을 Extract하여 RBAC 구현
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class UserRoleAspect {
    private final HttpServletRequest request;

    @Around("@annotation(checkUserRole)")
    public Object roleCheck(ProceedingJoinPoint joinPoint, CheckUserRole checkUserRole) throws Throwable {

        AccessLevel accessLevel = checkUserRole.value();

        // access level all is all pass
        if (accessLevel == AccessLevel.ALL) {
            return joinPoint.proceed();
        }

        String role = request.getHeader("X-User-Role");
        if (role == null) {
            throw new CustomException(ResponseCode.NEED_LOGIN);
        }

        String userRole = role.trim().toUpperCase();

        boolean result = switch (accessLevel){
            case MASTER ->  userRole.equals("MASTER");
            case MANAGER -> userRole.equals("MANAGER");
            case MANAGER_OR_MASTER ->  userRole.equals("MANAGER") || userRole.equals("MASTER");
            case USER -> userRole.equals("USER");
            default -> false;
        };

        if (!result) {
            throw new CustomException(ResponseCode.FORBIDDEN);
        }

        return joinPoint.proceed();
    }
}