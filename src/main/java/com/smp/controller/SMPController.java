package com.smp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class SMPController {
	
	//메인페이지
	@GetMapping("/main")
	public String main() {
		return "main";
	}
	
}
