package com.smp.service;

import org.springframework.stereotype.Service;

import com.smp.entity.SMP_Answer;
import com.smp.entity.SMP_Question;
import com.smp.repository.SMP_AnswerRepository;
import com.smp.repository.SMP_QuestionRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SMP_AnswerService {
	
	private final SMP_AnswerRepository smp_AnswerRepository;
	private final SMP_QuestionRepository smp_QuestionRepository;
	
	public void createAnswer(Long questionId, String body) {
      	// 1. 질문 엔티티 조회 (답변을 달 대상 질문 확인)
		SMP_Question question = smp_QuestionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 질문입니다. ID: " + questionId));

        // 2. 답변 엔티티 생성 및 질문 연결
        SMP_Answer answer = SMP_Answer.builder()
	        .smp_Question(question)
	        .body(body)
	        .status("ACTIVE")
	        .isAccepted("N")
	        .voteCount(0)
	        .commentCount(0)
	        .build();

        // 3. 답변 저장
        smp_AnswerRepository.save(answer);
        
        // 4. (선택사항) 질문 엔티티의 답변 개수(answerCount) 증가 로직
        // question.setAnswerCount(question.getAnswerCount() + 1);
 	}
}
	

