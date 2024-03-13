package org.example.restapi;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
public class AuditAspect {
    @Pointcut("execution(* org.example.restapi.RestApiController.*(..))")
    public void controllerMethods() {}

    @Before("controllerMethods()")
    public void beforeControllerMethod(JoinPoint joinPoint) {
        String method = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        String username = getCurUser();
        LocalDateTime actionTime = LocalDateTime.now();

        StringBuilder argsString = new StringBuilder();
        for (Object arg : args) {
            argsString.append(arg).append(", ");
        }
        if (!argsString.isEmpty()) {
            argsString.delete(argsString.length() - 2, argsString.length());
        }

        System.out.printf("Audit - Time: %s, Username: %s, Method: %s, Args: [%s]%n", actionTime, username, method, argsString);
    }

    private String getCurUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getName();
        }
        return "anon";
    }
}
