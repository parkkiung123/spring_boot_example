package com.example.demo.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoResourceFoundException.class) // 404 에러 처리
    public String handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("error", "Not Found");
        model.addAttribute("message", "페이지를 찾을 수 없습니다.");
        logger.error("404 Not Found: ", ex);
        return "error/error"; // templates/error/error.html 로 이동
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, HttpServletRequest request, Model model) {
        model.addAttribute("status", request.getAttribute("jakarta.servlet.error.status_code"));
        model.addAttribute("error", ex.getClass().getSimpleName());
        model.addAttribute("message", ex.getMessage());
        logger.error("Exception occurred: ", ex);
        return "error/error"; // templates/error/error.html 로 이동
    }
}

