package com.smp.entity;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SMP_NOTIFICATION")
@Getter
@Setter
@NoArgsConstructor
public class SMP_Notification {
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    // 수신자
	    @Column(nullable = false)
	    private Long receiver;

	    // 알림 내용
	    @Column(nullable = false)
	    private String message;

	    // 알림 타입 (INFO, WARNING, SUCCESS, ERROR)
	    @Enumerated(EnumType.STRING)
	    @Column(nullable = false)
	    private NotificationType type;

	    // 읽음 여부
	    @Column(nullable = false)
	    private boolean isRead = false;

	    // 생성 시각
	    @Column(nullable = false)
	    private LocalDateTime createdAt;

	    @PrePersist
	    protected void onCreate() {
	        this.createdAt = LocalDateTime.now();
	    }

	    public SMP_Notification(Long receiver, String message, NotificationType type) {
	        this.receiver = receiver;
	        this.message = message;
	        this.type = type;
	        this.createdAt = LocalDateTime.now();
	    }

	    public enum NotificationType {
	        INFO, SUCCESS, WARNING, ERROR
	    }
}
