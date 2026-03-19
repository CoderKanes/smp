package com.smp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

// 오류 해결이요 ^^ / 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/main", "/image/**", "/js/**", "/css/**", "/bootstrap/**",
                        "/question/**", "/user/login", "/user/create", "/error")
                .permitAll()
                .anyRequest().authenticated()
            )

            .headers((headers) -> headers
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
            )

            .formLogin((formLogin) -> formLogin
                .loginPage("/user/login")
                .loginProcessingUrl("/user/login") 
                .defaultSuccessUrl("/question/list")
                .permitAll()
            )

            .logout((logout) -> logout
                .logoutRequestMatcher(PathPatternRequestMatcher.pathPattern("/user/logout"))
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/question/list")
            )

            .exceptionHandling((exception) -> exception
                .accessDeniedPage("/user/denied")
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}