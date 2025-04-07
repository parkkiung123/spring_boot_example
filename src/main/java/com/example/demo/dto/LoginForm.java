package com.example.demo.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
public class LoginForm {

    @NotBlank(message = "아이디를 입력하세요.")
    private String id;

    @NotBlank(message = "이름을 입력하세요.")
    private String name;

    private String role; // "user (student)" or "admin (teacher)"
}
