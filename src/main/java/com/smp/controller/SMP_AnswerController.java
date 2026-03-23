package com.smp.controller;

import com.smp.service.SMP_AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/qna/answer")
@RequiredArgsConstructor
@Slf4j
public class SMP_AnswerController {

    private final SMP_AnswerService smp_AnswerService;

    @PostMapping("/create/{id}")
    public String createAnswer(@PathVariable("id") Long questionId, 
                               @RequestParam("body") String body, Principal principal) {
        log.info("[답변 등록] 질문 ID: {}, 내용: {}", questionId, body);
        
        // 답변 저장 서비스 호출
        smp_AnswerService.createAnswer(questionId, body, principal.getName());
        
        // 저장 후 다시 해당 질문의 상세 페이지로 리다이렉트
        return String.format("redirect:/qna/detail/%d", questionId);
    }
}