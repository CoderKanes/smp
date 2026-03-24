package com.smp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smp.entity.MemberEntity;
import com.smp.entity.SMP_Notification;
import com.smp.entity.SMP_Notification.NotificationType;
import com.smp.model.NotificationDTO;
import com.smp.repository.NotificationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final SseEmitterService sseEmitterService;
    private final MemberService memberService;

    /** 
     * 알림 생성 + DB 저장 + SSE 전송
     */
    @Transactional
    public NotificationDTO createAndSend(Long receiver, String message, NotificationType type) {
        // 1. DB에 저장
    	SMP_Notification notification = new SMP_Notification(receiver, message, type);
        notification = notificationRepository.save(notification);
        log.info("[알림] DB 저장 완료 - id: {}, receiver: {}", notification.getId(), receiver);

        NotificationDTO dto = new NotificationDTO(notification);

        // 2. SSE로 실시간 전송
        sseEmitterService.sendNotification(receiver, dto.toJson());

        return dto;
    }
    @Transactional
    public NotificationDTO createAndSendAll(String message, NotificationType type) {
        
    	List<MemberEntity> allUserEntity = memberService.findAllUser();
    	// 	1. DB에 저장
    	List<SMP_Notification> notifications = new ArrayList<>();
    	for(MemberEntity mem : allUserEntity)
    	{
    		SMP_Notification notification = new SMP_Notification(mem.getId(), message, type);
    		notifications.add(notification);
    	}   	
    	
    	notifications = notificationRepository.saveAll(notifications);      
    	
    
    	for(SMP_Notification noti : notifications)
    	{   
    		NotificationDTO dto = new NotificationDTO(noti);

    		// 2. SSE로 실시간 전송
    		sseEmitterService.sendNotification(noti.getReceiver(), dto.toJson());
    	}
    	
    	NotificationDTO resultdto = new NotificationDTO( new SMP_Notification((long) 0, message, type));

        return resultdto;
    }

    /**
     * 특정 사용자의 전체 알림 목록 조회
     */
    @Transactional(readOnly = true)
    public List<NotificationDTO> getNotifications(Long userId) {
        return notificationRepository
                .findByReceiverOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * 읽지 않은 알림 개수
     */
    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByReceiverAndIsReadFalse(userId);
    }

    /**
     * 알림 읽음 처리
     */
    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
            log.info("[알림] 읽음 처리 - id: {}", notificationId);
        });
    }

    /**
     * 모든 알림 읽음 처리
     */
    @Transactional
    public void markAllAsRead(Long userId) {
        List<SMP_Notification> unread = notificationRepository
                .findByReceiverAndIsReadFalseOrderByCreatedAtDesc(userId);
        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);
        log.info("[알림] 전체 읽음 처리 - user: {}, count: {}", userId, unread.size());
    }
}
