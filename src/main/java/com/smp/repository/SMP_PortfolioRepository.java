package com.smp.repository;

import com.smp.entity.SMP_Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SMP_PortfolioRepository extends JpaRepository<SMP_Portfolio, Long> {

    // 1. 특정 작성자(Member)의 ID로 포트폴리오 목록 조회 (마이페이지용)
    List<SMP_Portfolio> findByAuthorId(Long authorId);

    // 2. 제목에 특정 단어가 포함된 포트폴리오 검색
    List<SMP_Portfolio> findByTitleContaining(String title);

    // 3. 최근 등록된 순으로 전체 목록 조회
    List<SMP_Portfolio> findAllByOrderByCreatedAtDesc();
    
    List<SMP_Portfolio> findAllByOrderByIdDesc();
    
    // 작성자의 아이디로 리스트 찾기
    List<SMP_Portfolio> findByAuthor_Userid(String userid);

    // 2. 특정 사용자의 목록 (일반 유저용)
    // 연관관계인 author 엔티티 안의 userid 필드로 찾습니다.
    List<SMP_Portfolio> findByAuthor_UseridOrderByIdDesc(String userid);
}