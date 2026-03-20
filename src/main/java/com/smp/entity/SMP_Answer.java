package com.smp.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ManyToAny;

import com.smp.model.SMP_AnswerDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SMP_ANSWER", indexes = {
		@Index(name = "IDX_ANSWER_QUESTION", columnList = "question_id"),
		@Index(name = "IDX_ANSWER_ACCEPTED", columnList = "is_accepted")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SMP_Answer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", nullable = false)
	private SMP_Question smp_Question;
	
	@Lob
	@Column(name = "body", nullable = false)
	private String body;
	
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@Column(name = "is_accepted", length = 1, nullable = false)
	private String isAccepted = "N";
	
	@Column(name = "vote_count", nullable = false)
	private Integer voteCount = 0;
	
	@Column(name = "comment_count", nullable = false)
	private Integer commentCount = 0;
	
	@Column(name = "status", length = 20, nullable = false)
	private String status = "ACTIVE";
	
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;
	
	//questionid가져오기
	public Long getQuestionId() {
		SMP_Question q = this.getSmp_Question();
		return (q != null) ? q.getId() : null ;
	}
	
	 public SMP_AnswerDTO toDto() {
        SMP_AnswerDTO dto = new SMP_AnswerDTO();
        dto.setId(this.getId());
        dto.setQuestionId(this.getQuestionId());
        dto.setBody(this.getBody());
        dto.setCreatedAt(this.getCreatedAt());
        dto.setUpdatedAt(this.getUpdatedAt());
        dto.setIsAccepted(this.getIsAccepted());
        dto.setVoteCount(this.getVoteCount());
        dto.setCommentCount(this.getCommentCount());
        dto.setStatus(this.getStatus());
        dto.setDeletedAt(this.getDeletedAt());
        return dto;
    }
	
	
}
