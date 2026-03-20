package com.smp.service;

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

    /**
     * 질문 생성 및 DB 저장
     */
    @Transactional
    public SMP_QuestionDTO createQuestion(SMP_QuestionDTO dto) {
        // DTO -> Entity 변환 (빌더 패턴 사용)
        SMP_Question question = SMP_Question.builder()
                .title(dto.getTitle())
                .body(dto.getBody())
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
    public void updateQuestion(Long id, SMP_QuestionDTO dto) {
        SMP_Question question = questionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("수정할 질문이 없습니다."));
        
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
    public void deleteQuestion(Long id) {
        questionRepository.deleteById(id);
        log.info("[질문삭제] 삭제 완료 - ID: {}", id);
    }
    
    public Page<SMP_Question> getList(int page) {
        // page: 페이지 번호 (0부터 시작), 10: 한 페이지에 보여줄 게시글 수
        Pageable pageable = PageRequest.of(page, 10, Sort.by("createdAt").descending());
        return questionRepository.findAll(pageable);
    }
    
}