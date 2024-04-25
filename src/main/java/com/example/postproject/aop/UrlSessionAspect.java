package com.example.postproject.aop;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class UrlSessionAspect {

    // Member와 관련된 모든 엔드 포인트 제외
    @Around("execution(* com.example.postproject.controller..*(..)) " +
            "&& !execution(* com.example.postproject.controller.MemberController.*(..)) " +
            "&& !execution(* com.example.postproject.controller.PostController.search(..)) "
    )
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();

        String prevUrl = (String) session.getAttribute("prevUrl");
        String currentUrl = request.getRequestURL().toString();
        if (currentUrl.contains("/member") || currentUrl.contains("/search")) {
            session.setAttribute("prevUrl", prevUrl);
        } else {
            session.setAttribute("prevUrl", currentUrl);
        }
        Object proceed = joinPoint.proceed();

        return proceed;
    }
}