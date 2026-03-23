package com.smp.service;

import com.smp.entity.MemberEntity;
import com.smp.entity.SMP_Question;
import com.smp.repository.SMP_QuestionRepository;
import com.smp.model.SMP_QuestionDTO; // 아래에 작성해드리는 DTO 참고
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SMP_QuestionService {

    private final SMP_QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    /**
     * 질문 생성 및 DB 저장
     */
    @Transactional
    public SMP_QuestionDTO createQuestion(SMP_QuestionDTO dto) {
        MemberEntity member = memberRepository.findByUserid(dto.getAuthorUserid()).orElseThrow(() -> new RuntimeException("회원이 없어요"));
    	
    	// DTO -> Entity 변환 (빌더 패턴 사용)
        SMP_Question question = SMP_Question.builder()
                .title(dto.getTitle())
                .body(dto.getBody())
                .author(member)
                .isAnonymous(dto.getIsAnonymous() != null ? dto.getIsAnonymous() : "N")
                .isPinned(dto.getIsPinned() != null ? dto.getIsPinned() : "N")
                .status("OPEN")
                .viewCount(0L)
                .answerCount(0)
                .voteCount(0)
                .build();

        question = questionRepository.save(question);
        log.info("[질문등록] DB 저장 완료 - ID: {}, 제목: {}", question.getId(), question.getTitle());

        return new SMP_QuestionDTO(question);
    }

    /**
     * 상세 조회 (조회수 증가 포함)
     */
    @Transactional
    public SMP_QuestionDTO getQuestionDetail(Long id) {
        // 1. 조회수 증가 (Repository의 Modifying 쿼리 호출)
        questionRepository.updateViewCount(id);
        
        // 2. 데이터 조회
        SMP_Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 질문입니다. ID: " + id));
        
        log.info("[질문조회] ID: {}, 현재 조회수: {}", id, question.getViewCount() + 1);
        return new SMP_QuestionDTO(question);
    }

    /**
     * 전체 질문 목록 조회 (최신순)
     */
    @Transactional(readOnly = true)
    public List<SMP_QuestionDTO> getAllQuestions() {
        return questionRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(SMP_QuestionDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 고정된 질문 목록 조회
     */
    @Transactional(readOnly = true)
    public List<SMP_QuestionDTO> getPinnedQuestions() {
        return questionRepository.findByIsPinnedOrderByCreatedAtDesc("Y")
                .stream()
                .map(SMP_QuestionDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 질문 수정
     */
    @Transactional
    public void updateQuestion(Long id, SMP_QuestionDTO dto, String loginUserid) {
        SMP_Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정할 질문이 없습니다."));
        
        //본인확인
        if(question.getAuthor().getUserid().equals(loginUserid)) {
        	throw new RuntimeException("본인이 작성한 글만 수정할 수 있습니다");
        }
        
        question.setTitle(dto.getTitle());
        question.setBody(dto.getBody());
        question.setIsAnonymous(dto.getIsAnonymous());
        question.setIsPinned(dto.getIsPinned());
        
        log.info("[질문수정] 수정 완료 - ID: {}", id);
    }

    /**
     * 질문 삭제 (Soft Delete 처리 시 deletedAt 업데이트, 여기서는 물리 삭제 예시)
     */
    @Transactional
    public void deleteQuestion(Long id, String loginUserid) {
    	SMP_Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 질문이 존재하지 않습니다. ID: " + id));
    	
        
    	//본인 확인
    	if(!question.getAuthor().getUserid().equals(loginUserid)) {
    		throw new RuntimeException("본인 글만 삭제할 수 있습니다");
    	}
    	
    	questionRepository.deleteById(id);
        log.info("[질문삭제] 삭제 완료 - ID: {}", id);
    }
    
    public Page<SMP_QuestionDTO> getList(int page) {
        // 1. 페이지 요청 설정 (최신순)
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        
        // 2. 리포지토리에서 엔티티 페이지 조회
        Page<SMP_Question> questionPage = questionRepository.findAll(pageable);
        
        // 3. 🔥 중요: 엔티티 페이지를 DTO 페이지로 변환
        // SMP_QuestionDTO 생성자(SMP_Question)를 사용하여 변환합니다.
        return questionPage.map(SMP_QuestionDTO::new);
    }
    
}