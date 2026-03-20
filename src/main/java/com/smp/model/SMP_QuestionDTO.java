package com.smp.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.smp.entity.SMP_Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SMP_QuestionDTO {
    
    private Long id;
    private String title;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long viewCount;
    private String status;
    private Integer answerCount;
    private Integer voteCount;
    private Long acceptedAnswerId;
    private String isAnonymous;
    private String isPinned;
    private LocalDateTime deletedAt;
    
    // ✅ 추가된 필드: 작성자 정보
    private Long authorId;      // MemberEntity의 PK (숫자)
    private String authorUserid; // MemberEntity의 userid (문자열)
    
    private List<SMP_AnswerDTO> answerList;
    
    // 생성자 수정
    public SMP_QuestionDTO(SMP_Question smp_Question) {
        this.id = smp_Question.getId();
        this.title = smp_Question.getTitle();
        this.body = smp_Question.getBody();
        this.createdAt = smp_Question.getCreatedAt();
        this.updatedAt = smp_Question.getUpdatedAt();
        this.viewCount = smp_Question.getViewCount();
        this.status = smp_Question.getStatus();
        this.answerCount = smp_Question.getAnswerCount();
        this.voteCount = smp_Question.getVoteCount();
        this.acceptedAnswerId = smp_Question.getAcceptedAnswerId();
        this.isAnonymous = smp_Question.getIsAnonymous();
        this.isPinned = smp_Question.getIsPinned();
        this.deletedAt = smp_Question.getDeletedAt();
        
        // ✅ 작성자 정보 매핑 (null 체크 필수)
        if (smp_Question.getAuthor() != null) {
            this.authorId = smp_Question.getAuthor().getId();
            this.authorUserid = smp_Question.getAuthor().getUserid();
        }
        
        if(smp_Question.getAnswerList() != null) {
            this.answerList = smp_Question.getAnswerList().stream()
                .map(SMP_AnswerDTO::new)
                .collect(Collectors.toList());
        }
    }
}