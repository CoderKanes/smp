package com.smp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.smp.handler.ChatHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer  {

	@Autowired
	private ChatHandler chatHandler; 
	
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		 System.out.println("일단 연결은 된다응");
		registry.addHandler(chatHandler, "/ws/chat").setAllowedOrigins("*");
	}

}