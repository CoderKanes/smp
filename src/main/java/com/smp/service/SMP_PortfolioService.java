package com.smp.service;

import com.smp.entity.MemberEntity;
import com.smp.entity.SMP_Portfolio;
import com.smp.model.SMP_PortfolioDTO;
import com.smp.service.MemberRepository; // 추가
import com.smp.repository.SMP_PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SMP_PortfolioService {

    private final SMP_PortfolioRepository portfolioRepository;
    private final MemberRepository memberRepository; // 추가

    @Transactional
    public void savePortfolio(SMP_PortfolioDTO dto, String userid) throws IOException {
        // 1. 로그인한 사용자(userid)로 MemberEntity 조회
        MemberEntity author = memberRepository.findByUserid(userid)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        MultipartFile file = dto.getFile();
        
        if (file != null && !file.isEmpty()) {
            // 저장 경로 (src/main/resources/static/portfolios/)
            String absolutePath = new File("src/main/resources/static/portfolios/").getAbsolutePath();
            
            String originName = file.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            String extension = originName.substring(originName.lastIndexOf("."));
            String saveName = uuid + extension;

            File folder = new File(absolutePath);
            if (!folder.exists()) folder.mkdirs();

            File saveFile = new File(absolutePath, saveName);
            file.transferTo(saveFile);

            // 2. DB 저장 (조회한 author 엔티티 포함)
            SMP_Portfolio portfolio = SMP_Portfolio.builder()
                    .title(dto.getTitle())
                    .originName(originName)
                    .saveName(saveName)
                    .filePath("/portfolios/" + saveName)
                    .fileSize(file.getSize())
                    .author(author) // 실제 DB의 Member와 연결
                    .createdAt(LocalDateTime.now())
                    .build();

            portfolioRepository.save(portfolio);
        }
    }
    
    @Transactional(readOnly = true)
    public List<SMP_Portfolio> findAllPortfolios() {
        return portfolioRepository.findAllByOrderByIdDesc();
    }
    
    @Transactional(readOnly = true)
    public List<SMP_Portfolio> getPortfoliosByRole(String userid, boolean isAdmin) {
        if (isAdmin) {
            // 관리자라면 모든 포트폴리오를 가져옴 (최신순)
            return portfolioRepository.findAllByOrderByIdDesc();
        } else {
            // 일반 유저라면 본인이 작성한 포트폴리오만 가져옴 (리포지토리에 메서드 추가 필요)
            return portfolioRepository.findByAuthor_UseridOrderByIdDesc(userid);
        }
    }
    
}