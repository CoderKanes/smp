package com.smp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SMP_PortfolioDTO {

    private Long id;              // 포트폴리오 식별자
    
    private String title;         // 제목
    
    private String originName;    // 원본 파일명
    
    private String saveName;      // 서버 저장 파일명
    
    private String filePath;      // 저장 경로
    
    private Long fileSize;        // 파일 크기
    
    private String authorName;    // 작성자 이름 (화면 표시용)
    
    private Long authorId;        // 작성자 고유 ID
    
    private LocalDateTime createdAt; // 등록일
    
    // 업로드 시 파일을 담기 위한 필드 (DB 저장용 아님)
    private MultipartFile file;
}