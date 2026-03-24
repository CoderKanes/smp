package com.smp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.smp.entity.SMP_Notification;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<SMP_Notification, Long> {

    // 특정 사용자의 모든 알림 (최신순)
    List<SMP_Notification> findByReceiverOrderByCreatedAtDesc(Long receiver);

    // 특정 사용자의 읽지 않은 알림
    List<SMP_Notification> findByReceiverAndIsReadFalseOrderByCreatedAtDesc(Long receiver);

    // 읽지 않은 알림 개수
    long countByReceiverAndIsReadFalse(Long receiver);
}
