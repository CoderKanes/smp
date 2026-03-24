package com.smp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SMP_Portfolio {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    private String title;          // 포트폴리오 제목 (예: "2026 하반기 포트폴리오")
    
    private String originName;     // 원본 파일명 (예: "내포트폴리오.pdf")
    
    private String saveName;       // 서버 저장 파일명 (UUID_내포트폴리오.pdf - 중복 방지)
    
    private String filePath;       // 저장 경로
    
    private Long fileSize;		   // 파일크기
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private MemberEntity author;   // 제출자 (로그인한 사용자)

    private LocalDateTime createdAt;
}
