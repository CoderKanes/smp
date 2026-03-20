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
	
	public SMP_AnswerDTO(SMP_Answer smp_Answer) {
		if (smp_Answer == null) return;
		this.id = smp_Answer.getId();
		this.questionId = smp_Answer.getQuestionId(); // 안전하게 id만 획득
		this.body = smp_Answer.getBody();
		this.createdAt = smp_Answer.getCreatedAt();
		this.updatedAt = smp_Answer.getUpdatedAt();
		this.isAccepted = smp_Answer.getIsAccepted();
		this.voteCount = smp_Answer.getVoteCount();
		this.commentCount = smp_Answer.getCommentCount();
		this.status = smp_Answer.getStatus();
		this.deletedAt = smp_Answer.getDeletedAt();
	}
	
}
