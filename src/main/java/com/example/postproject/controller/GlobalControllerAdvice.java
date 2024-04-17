package com.example.postproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn(HttpServletRequest request) {
        Boolean isLoggedIn = (Boolean) request.getSession().getAttribute("isLoggedIn");
        return isLoggedIn != null && isLoggedIn;
    }

}
