package com.lolsearch.lolrecordsearch.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class HashMapChatRoomRegistry implements ChatRoomWebSocketRegistry {
    
    private final Map<Long, List<WebSocketSession>> chatRoomWebSocketMap = new ConcurrentHashMap<>(); // 채팅방 별 구독자 리스트
    private final Map<String, Long> sessionChatRoomIdMap = new ConcurrentHashMap<>(); // 세션이 속한 방번호
    
    @Override
    public void registerWebSocket(Long chatRoomId, WebSocketSession session) {
        chatRoomWebSocketMap.compute(chatRoomId, (k, list) -> {
            if(list == null) list = new CopyOnWriteArrayList<>();
        
            list.add(session);
            return list;
        });
        sessionChatRoomIdMap.put(session.getId(), chatRoomId);
    }
    
    @Override
    public void removeWebSocket(Long chatRoomId, String sessionId) {
        removeSessionFromWebSocketMap(chatRoomId, sessionId);
    
        sessionChatRoomIdMap.remove(sessionId);
    }
    
    private void removeSessionFromWebSocketMap(Long chatRoomId, String sessionId) {
        if(chatRoomId == null) {
            return;
        }
        List<WebSocketSession> webSocketSessions = chatRoomWebSocketMap.get(chatRoomId);
        
        for (int i = 0; i < webSocketSessions.size(); i++) {
            WebSocketSession webSocketSession = webSocketSessions.get(i);
            if(isEqualsSessionId(sessionId, webSocketSession.getId())) {
                webSocketSessions.remove(i);
                break;
            }
        }
    }
    
    private boolean isEqualsSessionId(String sessionId1, String sessionId2) {
        return sessionId1.equals(sessionId2);
    }
    
    @Override
    public Long getChatRoomId(String sessionId) {
        return sessionChatRoomIdMap.get(sessionId);
    }
    
    @Override
    public List<WebSocketSession> getChatRoomWebSocketSessions(Long chatRoomId) {
        return chatRoomWebSocketMap.get(chatRoomId);
    }
}
