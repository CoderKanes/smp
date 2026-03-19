package com.smp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")

                .requestMatchers(
                        "/main",
                        "/image/**",
                        "/js/**",
                        "/css/**",
                        "/bootstrap/**",
                        "/question/**",
                        "/member/login",
                        "/member/signup",
                        "/error",
                        "/chat/**"
                ).permitAll()

                .anyRequest().authenticated()
            )

            .headers((headers) -> headers
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
            )

            .formLogin((formLogin) -> formLogin
                .loginPage("/member/login")
                .loginProcessingUrl("/member/login")
                .defaultSuccessUrl("/member/home")
                .permitAll()
            )

            .logout((logout) -> logout
                .logoutRequestMatcher(PathPatternRequestMatcher.pathPattern("/member/logout"))
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/member/login")
            )

            .exceptionHandling((exception) -> exception
                .accessDeniedPage("/member/login")
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}