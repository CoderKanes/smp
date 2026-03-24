package com.smp.controller;

import com.smp.entity.MemberEntity;
import com.smp.entity.SMP_Portfolio;
import com.smp.model.SMP_PortfolioDTO;
import com.smp.service.SMP_PortfolioService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/portfolio")
@RequiredArgsConstructor
public class SMP_PortfolioController {

    private final SMP_PortfolioService portfolioService;

    @GetMapping("/upload")
    public String uploadPage() {
        return "portfolio/upload";
    }

    @PostMapping("/upload")
    public String uploadPortfolio(SMP_PortfolioDTO dto, 
                                 @AuthenticationPrincipal UserDetails userDetails, // 시큐리티 인증 객체
                                 RedirectAttributes redirectAttributes) {
        
        if (userDetails == null) {
            return "redirect:/login"; // 로그인 안 되어 있으면 로그인 페이지로
        }

        try {
            // 서비스단에서 유저 정보를 처리할 수 있도록 전달
            // userDetails.getUsername()은 보통 ID(email 등)를 반환합니다.
            portfolioService.savePortfolio(dto, userDetails.getUsername());
            redirectAttributes.addFlashAttribute("message", "포트폴리오가 성공적으로 등록되었습니다.");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "파일 저장 오류: " + e.getMessage());
            return "redirect:/portfolio/upload";
        }

        return "redirect:/portfolio/list";
    }
    
    @GetMapping("/list")
    public String listPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String userid = userDetails.getUsername();
        
        // 사용자가 ADMIN 권한을 가지고 있는지 확인
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        // 이제 서비스에 메서드를 만들었으므로 에러가 사라집니다!
        List<SMP_Portfolio> list = portfolioService.getPortfoliosByRole(userid, isAdmin);
        
        model.addAttribute("portfolios", list);
        return "portfolio/list";
    }
    
}