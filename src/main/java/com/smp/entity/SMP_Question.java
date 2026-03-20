package com.smp.entity;



import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.smp.model.SMP_AnswerDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SMP_QUESTION", indexes = {
		@Index(name = "IDX_SPM_QUESTION_CREATED", columnList = "created_at"),
		@Index(name = "IDX_SMP_QUESTION_STATUS", columnList = "status"),
		@Index(name = "IDX_SMP_QUESTION_ACCEPTED", columnList = "accepted_answer_id")
})

public class SMP_Question {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;					//인덱스
	
	@Column(length = 255, nullable = false)
	private String title;				//제목
	
	@Column(length = 4000, name = "body", nullable = false)
	private String body;				//내용
	
	@CreationTimestamp
	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;	//등록일
	
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;	//업데이트
	
	@Builder.Default
	@Column(name = "view_count", nullable = false)
	private Long viewCount = 0L;		//조회수
	
	@Builder.Default
	@Column(nullable = false, length = 20)
	private String status = "OPEN";		//상태
	
	@Builder.Default
	@Column(name = "answer_count", nullable = false)
	private Integer answerCount = 0;	//답변
	
	@Builder.Default
	@Column(name = "vote_count", nullable = false)
	private Integer voteCount = 0;		//추천수
	
	@Column(name = "accepted_answer_id")
	private Long acceptedAnswerId;		//채택된 답변
	
	@Builder.Default
	@Column(name = "is_anonymous", length = 1, nullable = false)
	private String isAnonymous = "N";	//익명 여부
	
	@Builder.Default
	@Column(name = "is_pinned", length = 1, nullable = false)
	private String isPinned = "N";		//질문 고정상태
	
	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;	//삭제일
	
	@OneToMany(mappedBy = "smp_Question", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SMP_Answer> answerList;
	
	@ManyToOne
	@JoinColumn(name = "author_id")
	private MemberEntity author;
	
	public SMP_Question(String title, String body) {
        this.title = title;
        this.body = body;
        this.createdAt = LocalDateTime.now();
        this.viewCount = 0L;
        this.status = "OPEN";
        this.answerCount = 0;
        this.voteCount = 0;
        this.isAnonymous = "N";
        this.isPinned = "N";
    }
	
}
