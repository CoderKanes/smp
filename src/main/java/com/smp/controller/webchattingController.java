package com.smp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/chat") 
public class webchattingController {
	
    @GetMapping("/rooms") 
    public String roomsPage() {
        return "Chat/rooms"; 
    }
    @GetMapping("/room")
    public String chatRoom(@RequestParam("username") String username, Model model) {
        model.addAttribute("username", username);
        return "chat/chat"; 
    }
}