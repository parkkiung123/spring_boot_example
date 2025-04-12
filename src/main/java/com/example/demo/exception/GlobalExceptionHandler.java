package com.example.demo.exception;

import org.springframework.ui.Model;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class) // 404 에러 처리
    public String handleNoResourceFoundException(NoResourceFoundException ex, HttpServletRequest request, Model model) {
        model.addAttribute("status", 404);
        model.addAttribute("error", "Not Found");
        model.addAttribute("message", "페이지를 찾을 수 없습니다.");
        log.error("404 Not Found: ", ex);
        return "error/error"; // templates/error/error.html 로 이동
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, HttpServletRequest request, Model model) {
        model.addAttribute("status", request.getAttribute("jakarta.servlet.error.status_code"));
        model.addAttribute("error", ex.getClass().getSimpleName());
        model.addAttribute("message", ex.getMessage());
        log.error("Exception occurred: ", ex);
        return "error/error"; // templates/error/error.html 로 이동
    }
}

