package com.smp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.RequiredArgsConstructor;
import com.smp.service.MemberService;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController { 

    private final MemberService memberService;

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

    // ✅ 회원가입 처리 (하나만 유지)
    @PostMapping("/signup")
    public String signup(
            @RequestParam("userid") String userid,
            @RequestParam("password") String password) {

        memberService.signup(userid, password);
        return "redirect:/member/login";
    }

    // ✅ 로그인 처리
    @PostMapping("/login")
    public String loginProcess(
            @RequestParam("username") String username,
            @RequestParam("password") String password) {

        System.out.println("로그인 시도: " + username);

        // ❗ 실제 로그인은 Spring Security가 처리해야 함
        return "main";
    }

    // 메인 페이지
    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }
}