package com.smp.controller;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping; // 🔥 추가
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import com.smp.service.MemberService; // 🔥 추가

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor // 🔥 추가
public class MemberController { 

    private final MemberService memberService; // 🔥 추가

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "member/login";
    }

    // 회원가입 페이지
    @GetMapping("/signup")
    public String signupPage() {
        return "member/signup";
    }

    // 🔥🔥🔥 회원가입 처리 추가 (핵심)
    @PostMapping("/signup")
    public String signup(String userid, String password) {

        memberService.signup(userid, password);

        return "redirect:/member/login";
    }

    
    @PostMapping("/member/login")
    public String loginProcess(String username, String password) {
        // 1. DB에서 사용자 확인 (Service 호출)
        // 2. 성공하면?
        return "main"; // templates/main.html 을 찾아가게 됩니다!
    }

}