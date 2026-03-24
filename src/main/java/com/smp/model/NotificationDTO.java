package com.smp.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.smp.entity.SMP_Notification;

@Getter
public class NotificationDTO {

    private final Long id;
    private final Long receiver;
    private final String message;
    private final String type;
    private final boolean isRead;
    private final String createdAt;

    public NotificationDTO(SMP_Notification notification) {
        this.id = notification.getId();
        this.receiver = notification.getReceiver();
        this.message = notification.getMessage();
        this.type = notification.getType().name();
        this.isRead = notification.isRead();
        this.createdAt = notification.getCreatedAt()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // SSE 이벤트로 전송할 JSON 형태 문자열
    public String toJson() {
        return String.format(
            "{\"id\":%d,\"receiver\":\"%s\",\"message\":\"%s\",\"type\":\"%s\",\"isRead\":%b,\"createdAt\":\"%s\"}",
            id, receiver, message.replace("\"", "\\\""), type, isRead, createdAt
        );
    }
}
