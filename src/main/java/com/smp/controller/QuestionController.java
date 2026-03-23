package com.smp.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smp.model.SMP_QuestionDTO;
import com.smp.service.SMP_QuestionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/qna")
public class QuestionController {
    
    private final SMP_QuestionService smp_QuestionService;
    
    /**
     * 1. 질문 목록 조회
     */
    @GetMapping("/list")
    public String questionList(Model model, @RequestParam(value="page", defaultValue="0") int page) {
        List<SMP_QuestionDTO> pinnedList = smp_QuestionService.getPinnedQuestions();
        model.addAttribute("pinnedQuestions", pinnedList);
        
        Page<SMP_QuestionDTO> paging = smp_QuestionService.getList(page);
        model.addAttribute("paging", paging);
        
        return "qna/questionList"; 
    }

    /**
     * 2. 질문 상세 조회
     */
    @GetMapping("/detail/{id}")
    public String questionDetail(@PathVariable("id") Long id, Model model) {
        SMP_QuestionDTO dto = smp_QuestionService.getQuestionDetail(id);
        model.addAttribute("question", dto);
        return "qna/questionDetail";
    }

    /**
     * 3. 질문 작성 폼 이동
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/question")
    public String questionForm(Model model) {
        model.addAttribute("questionDTO", new SMP_QuestionDTO());
        return "qna/questionForm";
    }
    
    /**
     * 4. 질문 저장 처리
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/question")
    public String createQuestion(@ModelAttribute("questionDTO") SMP_QuestionDTO questionDTO, Principal principal) {
        // 로그인한 아이디 설정
        questionDTO.setAuthorUserid(principal.getName());
        smp_QuestionService.createQuestion(questionDTO);
        return "redirect:/qna/list"; 
    }

    /**
     * 5. 질문 수정 폼 이동 (작성자 본인 확인 포함)
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/modify/{id}")
    public String modifyForm(@PathVariable("id") Long id, Model model, Principal principal) {
        SMP_QuestionDTO dto = smp_QuestionService.getQuestionDetail(id);
        
        // 🔥 보안 체크: 작성자와 로그인 사용자가 다르면 리스트로 튕겨내기
        if (!dto.getAuthorUserid().equals(principal.getName())) {
            log.warn("[수정거부] 작성자가 아닙니다. 요청자: {}", principal.getName());
            return "redirect:/qna/list";
        }
        
        model.addAttribute("questionDTO", dto);
        return "qna/questionForm"; 
    }

    /**
     * 6. 질문 수정 실행 (서비스에서 본인 확인 로직 수행)
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/modify/{id}")
    public String updateQuestion(@PathVariable("id") Long id, 
                                 @ModelAttribute("questionDTO") SMP_QuestionDTO dto, 
                                 Principal principal) {
        
        // 서비스에 현재 로그인한 아이디(principal.getName())를 같이 넘겨줍니다.
        smp_QuestionService.updateQuestion(id, dto, principal.getName());
        
        return "redirect:/qna/detail/" + id;
    }

    /**
     * 7. 질문 삭제 (서비스에서 본인 확인 로직 수행)
     */
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable("id") Long id, Principal principal) {
        
        // 서비스에 현재 로그인한 아이디를 넘겨서 삭제 권한 체크
        smp_QuestionService.deleteQuestion(id, principal.getName());
        
        return "redirect:/qna/list";
    }
}