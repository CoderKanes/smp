package com.smp.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.smp.entity.MemberEntity;
import com.smp.entity.SMP_Notification.NotificationType;
import com.smp.model.MemberDTO;
import com.smp.model.NotificationDTO;
import com.smp.service.MemberService;
import com.smp.service.NotificationService;


import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/notice/**")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    
    private final MemberService memberService;

    @GetMapping("notiAdmin")
    public String admin(Model model) {
    	 List<MemberDTO> allusers = memberService.findAllUser().stream().map(MemberDTO::new).collect(Collectors.toList());
    	 model.addAttribute("allUsers",allusers);
    	 
        return "notice/notiAdmin";
    }
    
    @PreAuthorize("isAuthenticated()")
    @GetMapping("notiBoard")
    public String dashboard(Model model, Principal principal) {
    	
    	String name = principal.getName(); //로그인된 username
		System.out.println("로그인 유저명 : "+ name);
		
		MemberEntity entity= memberService.findByUserid(name);
		Long userId = entity.getId();
        List<NotificationDTO> notifications = notificationService.getNotifications(userId);
        long unreadCount = notificationService.getUnreadCount(userId);

        model.addAttribute("userid", userId);
        model.addAttribute("username", name);  
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);
        return "notice/notiBoard";
    }


}
