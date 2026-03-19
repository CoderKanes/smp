package com.smp.controller;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member/*")
public class MemberController {

    @GetMapping("login")
    public String loginPage() {
        return "member/login";
    }

    @GetMapping("signup")
    public String signupPage() {
        return "member/signup";
    }
    @GetMapping("/home")
    public String home(Model model, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) return "redirect:/login";

        Map<String, Object> attr = principal.getAttributes();
        String nick = null;
        String image = null;

        // 플랫폼별 주머니(Map) 열기
        if (attr.get("kakao_account") != null) {
            Map<String, Object> account = (Map<String, Object>) attr.get("kakao_account");
            Map<String, Object> profile = (Map<String, Object>) account.get("profile");
            nick = (String) profile.get("nickname");
            image = (String) profile.get("profile_image_url");

        } else if (attr.get("response") != null) {
            // 네이버 정보 추출
            Map<String, Object> response = (Map<String, Object>) attr.get("response");
            nick = (String) response.get("nickname");
            image = (String) response.get("profile_image");

        } else {
            // 구글 정보 추출
            nick = (String) attr.get("name");
            image = (String) attr.get("picture");
        }

        model.addAttribute("nickname", nick);
        model.addAttribute("profileImage", image);
        return "main";
    }
}