package com.smp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/chat") 
public class webchattingController {
	
    /**
     * 1. 채팅방 입장 화면 (닉네임 입력창)
     * 접속 주소: http://localhost:8080/chat/room
     */
    @GetMapping("/room") 
    public String rooms() {
        // 🔥 수정: 이제 templates/chat/rooms.html 을 정확히 찾아갑니다.
        // (발음: 리턴 챗 슬래시 룸스)
        return "chat/rooms"; 
    }

    /**
     * 2. 실제 채팅창 화면
     * 접속 주소: http://localhost:8080/chat/rooms?username=이름
     */
    @GetMapping("/rooms")
    public String chatRoom(@RequestParam("username") String username, Model model) {
        model.addAttribute("username", username);
        
        // 🔥 수정: 이제 templates/chat/chat.html 을 정확히 찾아갑니다.
        // (발음: 리턴 챗 슬래시 챗)
        return "chat/chat"; 
    }
}