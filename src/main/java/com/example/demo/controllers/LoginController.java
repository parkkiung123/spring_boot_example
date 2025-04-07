package com.example.demo.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

import com.example.demo.dto.LoginForm;

@Controller
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final AuthenticationManager authenticationManager;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(value = "/login/{role}", method = RequestMethod.POST)
    public String login(@PathVariable String role,
                        @ModelAttribute("loginForm") @Valid LoginForm form,
                        BindingResult bindingResult,
                        HttpSession session,
                        HttpServletRequest request,
                        HttpServletResponse response,
                        Model model) {
        if (bindingResult.hasErrors()) {
            logger.info("로그인 실패: " + bindingResult.getFieldError().getDefaultMessage());
            model.addAttribute("role", role);
            return "login"; // 로그인 페이지로 다시 이동
        } else {
            try {
                Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                        form.getId() + ":" + role,
                        form.getName()
                    )
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                new HttpSessionSecurityContextRepository().saveContext(
                    SecurityContextHolder.getContext(), request, response
                );
                logger.info("로그인 성공: " + form.getId() + ", " + form.getName());
                form.setRole(role);
                session.setAttribute("loginUser", form);
                return "redirect:/";
            } catch (AuthenticationException e) {
                logger.info("로그인 실패: " + form.getId() + ", " + form.getName());
                model.addAttribute("errorMessage", "아이디 또는 이름이 일치하지 않습니다.");
                model.addAttribute("role", role);
                return "login"; // 로그인 페이지로 다시 이동
            }
        }
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginForm(@RequestParam(required = false) String logout, Model model, HttpSession session) {
        if (logout != null) {
            logger.info("로그아웃 성공");
        }
        model.addAttribute("loginForm", new LoginForm());
        return "login";
    }
}
