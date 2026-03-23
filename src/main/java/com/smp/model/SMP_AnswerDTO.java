package com.smp.model;

import java.time.LocalDateTime;
import com.smp.entity.SMP_Answer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SMP_AnswerDTO {
    
    private Long id;
    private Long questionId;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String isAccepted;
    private Integer voteCount;
    private Integer commentCount;
    private String status;
    private LocalDateTime deletedAt;
    
    private String authorUserid; // 필드는 있는데!
    
    public SMP_AnswerDTO(SMP_Answer smp_Answer) {
        if (smp_Answer == null) return;
        this.id = smp_Answer.getId();
        this.questionId = smp_Answer.getQuestionId();
        this.body = smp_Answer.getBody();
        this.createdAt = smp_Answer.getCreatedAt();
        this.updatedAt = smp_Answer.getUpdatedAt();
        this.isAccepted = smp_Answer.getIsAccepted();
        this.voteCount = smp_Answer.getVoteCount();
        this.commentCount = smp_Answer.getCommentCount();
        this.status = smp_Answer.getStatus();
        this.deletedAt = smp_Answer.getDeletedAt();
        
        // 🔥 이 줄이 빠져있어서 아무리 불러도 안 나왔던 겁니다! 지금 추가하세요.
        this.authorUserid = smp_Answer.getAuthorUserid(); 
    }
}