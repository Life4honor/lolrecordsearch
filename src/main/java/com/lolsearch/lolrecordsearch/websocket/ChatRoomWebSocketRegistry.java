package com.lolsearch.lolrecordsearch.websocket;

import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface ChatRoomWebSocketRegistry {
    
    void registerWebSocket(Long chatRoomId, WebSocketSession session);
    
    void removeWebSocket(Long chatRoomId, String sessionId);
    
    Long getChatRoomId(String sessionId);
    
    List<WebSocketSession> getChatRoomWebSocketSessions(Long chatRoomId);
    
}
