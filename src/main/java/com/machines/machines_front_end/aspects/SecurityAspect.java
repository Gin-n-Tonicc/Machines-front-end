package com.machines.machines_front_end.aspects;

import com.machines.machines_front_end.annotations.PageRoleGuard;
import com.machines.machines_front_end.exceptions.RedirectException;
import com.machines.machines_front_end.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@AllArgsConstructor
public class SecurityAspect {
    private final HttpServletRequest httpServletRequest;
    private final SessionManager sessionManager;

    @Around("@annotation(pageRoleGuard)")
    public Object checkRole(ProceedingJoinPoint joinPoint, PageRoleGuard pageRoleGuard) throws Throwable {
        boolean proceed = sessionManager.isAuthenticated(httpServletRequest) == pageRoleGuard.authenticated();

        if (!pageRoleGuard.role().isEmpty()) {
            proceed = proceed && sessionManager.hasRole(httpServletRequest, pageRoleGuard.role());
        }

        proceed = sessionManager.isAdmin(httpServletRequest) || proceed;
        if (!proceed) {
            throw new RedirectException(pageRoleGuard.redirectTo());
        }

        // Proceed with the method invocation
        return joinPoint.proceed();
    }
}