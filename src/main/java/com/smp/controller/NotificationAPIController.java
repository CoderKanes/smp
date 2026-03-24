package com.smp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.smp.entity.SMP_Notification.NotificationType;
import com.smp.model.NotificationDTO;
import com.smp.service.NotificationService;
import com.smp.service.SseEmitterService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NotificationAPIController {

    private final SseEmitterService sseEmitterService;
    
    private final NotificationService notificationService;
    // ──────────────────────────────────────────────────────────────
    // SSE 구독 엔드포인트
    // ──────────────────────────────────────────────────────────────

    /**
     * SSE 구독
     * - 클라이언트가 EventSource로 연결 시 호출
     * - produces = text/event-stream 필수!
     */
 // ── SSE 구독 ──
    @GetMapping(value = "/api/notifications/subscribe/{userID}",
                produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public SseEmitter subscribe(@PathVariable("userID") Long userID) {
    	System.out.println("subscribe " + userID);
        return sseEmitterService.subscribe(userID);
    }

    // ── 알림 전송 ──
    @PostMapping("/api/notifications/send")
    @ResponseBody
    public ResponseEntity<NotificationDTO> sendNotification(
            @RequestParam("receiver") Long receiver,
            @RequestParam("message") String message,
            @RequestParam(name = "type", defaultValue = "INFO") NotificationType type) {
    	
    	System.out.println("sendNotification " + receiver +" "+ message);
    	
    	 NotificationDTO dto = null;
    	if(receiver == 0)
    	{
    		 dto = notificationService.createAndSendAll(message, type);
    	}else
    	{
    		  dto = notificationService.createAndSend(receiver, message, type);
    	}

      
        return ResponseEntity.ok(dto);
    }

    // ── 알림 목록 조회 ──
    @GetMapping("/api/notifications/{userID}")
    @ResponseBody
    public ResponseEntity<List<NotificationDTO>> getNotifications(@PathVariable("userID") Long userID) {
    	
    	System.out.println("getNotifications " + userID);
    	
        return ResponseEntity.ok(notificationService.getNotifications(userID));
    }

    @GetMapping("/api/notifications/{userID}/unread-count")
    @ResponseBody
    public ResponseEntity<Map<String, Long>> getUnreadCount(@PathVariable("userID") Long userID) {
    	
    	System.out.println("getUnreadCount " + userID);
    	
        long count = notificationService.getUnreadCount(userID);
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PatchMapping("/api/notifications/{notiID}/read")
    @ResponseBody
    public ResponseEntity<Void> markAsRead(@PathVariable("notiID") Long notiID) {
    	
    	System.out.println("markAsRead " + notiID);
    	
        notificationService.markAsRead(notiID);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/api/notifications/{userID}/read-all")
    @ResponseBody
    public ResponseEntity<Void> markAllAsRead(@PathVariable("userID") Long userID) {
    	
    	System.out.println("markAllAsRead " + userID);
    	
        notificationService.markAllAsRead(userID);
        return ResponseEntity.ok().build();
    }
}
