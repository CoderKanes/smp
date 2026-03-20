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

    // 1. 로그인 페이지 (GET)
    @GetMapping("/login")
    public String loginPage() {
        return "member/login"; // templates/member/login.html
    }

    // 2. 회원가입 페이지 (GET)
    @GetMapping("/signup")
    public String signupPage() {
        // 경로 대소문자 주의: member로 통일 권장
        return "member/signup"; 
    }

    // 3. 회원가입 처리 (POST)
    @PostMapping("/signup")
    public String signup(
            @RequestParam("userid") String userid,
            @RequestParam("password") String password) {

        memberService.signup(userid, password);
        // 회원가입 후 로그인 페이지로 이동 (경로 소문자 통일)
        return "redirect:/member/login";
    }

    /* 
     * ❗ 중요: @PostMapping("/login")은 삭제하거나 주석 처리하세요.
     * 스프링 시큐리티가 SecurityConfig의 .loginProcessingUrl("/member/login") 설정을 통해
     * 내부적으로 로그인 로직을 직접 처리합니다. 컨트롤러에 만들면 충돌이 날 수 있습니다.
     */

    // 4. 메인 페이지 (로그인 성공 후 이동할 곳)
    @GetMapping("/main")
    public String mainPage() {
        return "main"; // templates/main.html
    }

    // 5. 내 정보 페이지
    @GetMapping("/info")
    public String infoPage() {
        return "member/info";
    }
}