package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/login/**", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )
            .logout(logout -> logout
                .invalidateHttpSession(true) // 세션 무효화
                .clearAuthentication(true) // 인증 정보 삭제
                .deleteCookies("JSESSIONID") // 쿠키 삭제
            )
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint((request, response, authException) -> {
                    response.sendRedirect("/login"); // 인증 실패 시 /login 으로 redirect
                })
            );            
        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // @Bean
    // PasswordEncoder passwordEncoder() {
    //     return new BCryptPasswordEncoder();
    // }
    
    @SuppressWarnings("deprecation")
    @Bean
    PasswordEncoder passwordEncoder() {
        // 비밀번호 검증에 이름을 사용하고 있으므로 NoOp 사용 (실제 서비스에선 BCrypt 필수!)
        return NoOpPasswordEncoder.getInstance();
    }

}
