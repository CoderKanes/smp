package com.smp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SSE(Server-Sent Events) 연결을 관리하는 서비스
 * - 사용자별 SseEmitter를 ConcurrentHashMap에 보관
 * - 알림 발생 시 해당 사용자의 Emitter로 이벤트 전송
 */
@Slf4j
@Service
public class SseEmitterService {

    // SSE 연결 타임아웃: 30분
    private static final long SSE_TIMEOUT = 30 * 60 * 1000L;

    // username -> SseEmitter 맵 (동시성 안전)
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    /**
     * 클라이언트가 SSE 구독 요청 시 Emitter를 생성하고 반환
     */
    public SseEmitter subscribe(Long userId) {
        // 기존 연결이 있으면 종료
        if (emitters.containsKey(userId)) {
            emitters.get(userId).complete();
        }

        SseEmitter emitter = new SseEmitter(SSE_TIMEOUT);

        // 연결 완료/타임아웃/에러 시 맵에서 제거
        emitter.onCompletion(() -> {
            emitters.remove(userId);
            log.info("[SSE] 연결 종료 - user: {}", userId);
        });
        emitter.onTimeout(() -> {
            emitters.remove(userId);
            log.info("[SSE] 연결 타임아웃 - user: {}", userId);
        });
        emitter.onError(e -> {
            emitters.remove(userId);
            log.warn("[SSE] 연결 오류 - user: {}, error: {}", userId, e.getMessage());
        });

        emitters.put(userId, emitter);
        log.info("[SSE] 새 연결 등록 - user: {}, 현재 연결 수: {}", userId, emitters.size());

        // 연결 직후 초기 이벤트(연결 확인용) 전송
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("SSE 연결 완료 - " + userId));
        } catch (IOException e) {
            log.warn("[SSE] 초기 이벤트 전송 실패 - user: {}", userId);
        }

        return emitter;
    }

    /**
     * 특정 사용자에게 알림 이벤트 전송
     */
    public void sendNotification(Long userId, String data) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter == null) {
            log.info("[SSE] 연결된 클라이언트 없음 - user: {} (DB에는 저장됨)", userId);
            return;
        }

        try {
            emitter.send(SseEmitter.event()
                    .name("notification")   // 이벤트 이름 (클라이언트에서 addEventListener로 수신)
                    .data(data));           // JSON 데이터
            log.info("[SSE] 알림 전송 완료 - user: {}", userId);
        } catch (IOException e) {
            emitters.remove(userId);
            log.warn("[SSE] 알림 전송 실패 - user: {}, 연결 제거", userId);
        }
    }

    /**
     * 현재 연결된 사용자 수 반환 (모니터링용)
     */
    public int getConnectedCount() {
        return emitters.size();
    }
}
