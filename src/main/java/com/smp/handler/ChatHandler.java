package com.smp.handler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component; 
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

// 🔥 수정: 잘못된 tools.jackson 대신 스프링 표준 패키지를 사용합니다.
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ChatHandler extends TextWebSocketHandler{

   //접속한 모든 사용자 저장
   private final List<WebSocketSession> sessions = new ArrayList<>();
   
   // JSON <--> JAVA 변환 역할 
   private final ObjectMapper objectMapper = new ObjectMapper();
   
   @Override
   public void afterConnectionEstablished(WebSocketSession session) throws Exception {
      // 클라이언트가 웹소켓(채팅) 접속할때 한번 실행 
      System.out.println("접속됨 : "+session);
      sessions.add(session);
   }

   @Override
   protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
      // 메시지 수신
      System.out.println("메시지 수신 : "+message);
      
      String payload = message.getPayload();
       Map<String, Object> data = objectMapper.readValue(payload, Map.class);
       
       String target = (String)data.get("target"); // "ALL" 또는 "특정유저이름"
       String username = (String)data.get("username");
       
    // 귓속말 여부를 판단하여 다시 데이터에 넣음
       boolean isPrivate = !"ALL".equals(target);
       data.put("isPrivate", isPrivate);
       
    // 가공된 데이터를 다시 JSON 문자열로 변환
       String processedPayload = objectMapper.writeValueAsString(data);
       TextMessage newMessage = new TextMessage(processedPayload);

       // 1. 처음 입장 시 이름 저장 및 명단 업데이트 
       if (session.getAttributes().get("username") == null && username != null) {
           session.getAttributes().put("username", username);
           broadcastUserList();
       }

       // 2. 메시지 전송 (귓속말 , 전체)
       for (WebSocketSession s : sessions) {
           String sName = (String) s.getAttributes().get("username");
           
           if (!isPrivate) {
               s.sendMessage(newMessage); // 전체 전송
           } else {
               // 귓속말: 타겟이거나 나 자신일 때만 전송
               if (target.equals(sName) || username.equals(sName)) {
                   s.sendMessage(newMessage);
               }
           }
       }
   }

   @Override
   public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
      // 클라이언트 연결이 종료 되었을때
      System.out.println("연결종료 : "+session);
      sessions.remove(session);
      broadcastUserList();
   }
   
   // 접속 중인 모든 사용자 명단을 전송하는 메서드
    private void broadcastUserList() throws IOException {
        List<String> userList = sessions.stream()
                .map(s -> (String) s.getAttributes().getOrDefault("username", "익명"))
                .collect(Collectors.toList());

        // chat.html에서 활용할 type을 지정
        Map<String, Object> result = Map.of(
            "type", "USER_LIST",
            "userList", userList 
        );

        //JAVA --> JSON 변환 
        String json = objectMapper.writeValueAsString(result);
        TextMessage listMessage = new TextMessage(json);

        for (WebSocketSession s : sessions) {
            if (s.isOpen()) s.sendMessage(listMessage);
        }
    }
}