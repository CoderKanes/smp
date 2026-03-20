package com.smp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smp.entity.SMP_Question;

public interface SMP_QuestionRepository extends JpaRepository<SMP_Question, Long> {
	
	 // 1. 최신순 전체 조회 (목록용)
    List<SMP_Question> findAllByOrderByCreatedAtDesc();

    // 2. 상태별 조회 (OPEN, CLOSED 등 - 인덱스 활용)
    List<SMP_Question> findByStatusOrderByCreatedAtDesc(String status);

    // 3. 고정된(Pinned) 질문 상단 노출용
    List<SMP_Question> findByIsPinnedOrderByCreatedAtDesc(String isPinned);

    // 4. 조회수 증가 (상세보기 시 필수)
    @Modifying
    @Query("UPDATE SMP_Question q SET q.viewCount = q.viewCount + 1 WHERE q.id = :id")
    int updateViewCount(@Param("id") Long id);
    
    // 5. 제목으로 검색
    List<SMP_Question> findByTitleContainingOrderByCreatedAtDesc(String keyword);
    
    Page<SMP_Question> findAll(Pageable pageable);
	
}
