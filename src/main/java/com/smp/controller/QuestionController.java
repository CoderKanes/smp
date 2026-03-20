package com.smp.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smp.entity.SMP_Question;
import com.smp.model.SMP_QuestionDTO;
import com.smp.service.SMP_QuestionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/qna") // 사용하시는 기본 경로 유지
public class QuestionController {
	
	private final SMP_QuestionService smp_QuestionService;
	
	/**
	 * 1. 질문 목록 조회 (고정글 + 일반글)
	 * URL: http://localhost:8080/qna/list
	 */
	@GetMapping("/list")
	public String questionList(Model model, @RequestParam(value="page", defaultValue="0") int page) {
	    log.info("[조회] 질문 목록 요청 - 페이지 번호: {}", page);
	    
	    // 1. 고정된 질문(공지사항 등) 리스트는 상단에 항상 노출 (기존 유지)
	    List<SMP_QuestionDTO> pinnedList = smp_QuestionService.getPinnedQuestions();
	    model.addAttribute("pinnedQuestions", pinnedList);
	    
	    // 2. 일반 질문 리스트를 페이징 처리해서 가져옴 (수정된 부분)
	    // 서비스에서 리턴 타입을 Page<SMP_QuestionDTO> 또는 Page<SMP_Question>으로 받아야 합니다.
	    Page<SMP_Question> paging = smp_QuestionService.getList(page);
	    
	    // 3. 뷰(HTML)에서 사용할 이름을 "paging"으로 전달
	    model.addAttribute("paging", paging);
	    
	    return "qna/questionList"; 
	}

	/**
	 * 2. 질문 상세 조회 (조회수 증가 포함)
	 * URL: http://localhost:8080/qna/detail/{id}
	 */
	@GetMapping("/detail/{id}")
	public String questionDetail(@PathVariable("id") Long id, Model model) {
		log.info("[조회] 질문 상세 보기 - ID: {}", id);
		
		SMP_QuestionDTO dto = smp_QuestionService.getQuestionDetail(id);
		model.addAttribute("question", dto);
		
		return "qna/questionDetail"; // templates/qna/questionDetail.html
	}

	/**
	 * 3. 질문 작성 폼 이동 (GET)
	 * URL: http://localhost:8080/qna/question
	 */
	@GetMapping("/question")
	public String questionForm(Model model) {
		log.info("[화면] 질문 작성 폼 이동");
		// 빈 DTO를 넘겨서 타임리프 폼과 바인딩
		model.addAttribute("questionDTO", new SMP_QuestionDTO());
		
		return "qna/questionForm"; // templates/qna/questionForm.html
	}
	
	/**
	 * 4. 질문 실제 저장 처리 (POST)
	 */
	@PostMapping("/question")
	public String createQuestion(@ModelAttribute("questionDTO") SMP_QuestionDTO questionDTO) {
		log.info("[등록] 신규 질문 저장 - 제목: {}", questionDTO.getTitle());
		
		smp_QuestionService.createQuestion(questionDTO);
		
		// 저장 후 목록 페이지로 리다이렉트
		return "redirect:/qna/list"; 
	}

	/**
	 * 5. 질문 수정 폼 이동
	 */
	@GetMapping("/modify/{id}")
	public String modifyForm(@PathVariable("id") Long id, Model model) {
		log.info("[수정] 질문 수정 폼 요청 - ID: {}", id);
		
		SMP_QuestionDTO dto = smp_QuestionService.getQuestionDetail(id);
		model.addAttribute("questionDTO", dto);
		
		// 등록 폼과 같은 HTML을 사용하거나 별도의 수정 폼 사용 가능
		return "qna/questionForm"; 
	}

	/**
	 * 6. 질문 수정 실행 (POST)
	 */
	@PostMapping("/modify/{id}")
	public String updateQuestion(@PathVariable("id") Long id, @ModelAttribute("questionDTO") SMP_QuestionDTO dto) {
		log.info("[수정] 질문 수정 완료 - ID: {}", id);
		
		smp_QuestionService.updateQuestion(id, dto);
		
		return "redirect:/qna/detail/" + id;
	}

	/**
	 * 7. 질문 삭제
	 */
	@GetMapping("/delete/{id}")
	public String deleteQuestion(@PathVariable("id") Long id) {
		log.info("[삭제] 질문 삭제 실행 - ID: {}", id);
		
		smp_QuestionService.deleteQuestion(id);
		
		return "redirect:/qna/list";
	}
}
