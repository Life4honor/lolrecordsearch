package com.lolsearch.lolrecordsearch.websocket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearch.lolrecordsearch.dto.ChatMessage;
import com.lolsearch.lolrecordsearch.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CustomTextWebSocketHandler extends TextWebSocketHandler {
    
    private static final int FIRST_USER_MESSAGE_SIZE = -20;
    
    private final Map<Long, List<WebSocketSession>> subscribers = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> users = new ConcurrentHashMap<>();
    private final Set<String> disconnectors = new ConcurrentHashMap<>().newKeySet();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private ChatService chatService;
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Principal principal = session.getPrincipal();
        if(principal == null) {
            throw new IllegalStateException("로그인 해라!!");
        }
        
        log.info("============================== afterConnectionEstablished ===================================");
        
        Map<String, String> params = convertQueryStringToMap(session.getUri().getQuery());
        log.info("params : {}", params);
        //클라이언트에서 전송한 채팅방 아이디, 세션 담기
        Long chatRoomId = Long.valueOf(params.get("chatRoomId"));
        subscribers.compute(chatRoomId, (k, v) -> {
            if(v == null) {
                v = new CopyOnWriteArrayList<>();
            }
            v.add(session);
            return v;
        });
        log.info("============================== afterConnectionEstablished ===================================");
    }
    
    private Map<String, String> convertQueryStringToMap(String queryString) {
        return Arrays.stream(queryString.split("&"))
                    .map(s -> s.split("="))
                    .collect(Collectors.toMap(array -> array[0], array -> array[1]));
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        
        Map<String, String> chatMessageMap = convertStringMessageToMap(message.getPayload());
        
        ChatMessage chatMessage = convertMapToChatMessage(chatMessageMap);
    
        Long userId = (Long) session.getAttributes().get("userId");
        if(!userId.equals(chatMessage.getUserId())) {
            throw new IllegalArgumentException("유효하지 않은 사용자 입니다.");
        }
        
        // 몽고디비에 업데이트 후 업데이트값 전송
        chatService.saveChatMessage(chatMessage.getChatRoomId(), userId, chatMessage);
        
        //TODO 최초 접속자 구분 파라미터 isFirst -> type으로 변경 필요.
        Optional<String> firstUserSessionId = Optional.empty();
        if(isFirstUser(chatMessageMap)) {
            //TODO 몽고디비에 넣고 조회 해보기
            // 최초 연결자는 몽고디비에서 메시지 최근순으로 20건까지 조회하여 전송.
            List<ChatMessage> chatMessages = chatService.findChatMessages(chatMessage.getChatRoomId(), FIRST_USER_MESSAGE_SIZE);
    
            sendMessage(session, chatMessages);
            firstUserSessionId = Optional.of(session.getId());
        }
        
        // 세션아이디 리스트반복 돌면서 subscriber에서 일치하는 세션에게만 메시지 전송
        broadCastMessage(chatMessage.getChatRoomId(), firstUserSessionId, Arrays.asList(chatMessage));
    }
    
    private void sendMessage(WebSocketSession session, List<ChatMessage> chatMessages) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsBytes(chatMessages));
        session.sendMessage(textMessage);
    }
    
    private boolean isFirstUser(Map<String, String> chatMessageMap) {
        return chatMessageMap.get("isFirst") != null;
    }
    
    private void broadCastMessage(Long chatRoomId, Optional<String> firstUserSessionId, List<ChatMessage> chatMessages) throws IOException {
        List<WebSocketSession> subscriberSessions = subscribers.get(chatRoomId);
        for(int i = 0; i < subscriberSessions.size(); i++) {
            WebSocketSession webSocketSession = subscriberSessions.get(i);
        
            if(disconnectors.contains(webSocketSession.getId())) {
                subscriberSessions.remove(i);
                disconnectors.remove(webSocketSession.getId());
                continue;
            }
            
            if(isFirstUserSessionId(firstUserSessionId, webSocketSession.getId())) {
                continue;
            }
    
            sendMessage(webSocketSession, chatMessages);
        }
    }
    
    private boolean isFirstUserSessionId(Optional<String> firstUserSessionId, String webSocketSessionId) {
        return firstUserSessionId.isPresent() && webSocketSessionId.equals(firstUserSessionId.get());
    }
    
    private Map<String, String> convertStringMessageToMap(String chatMessage) throws IOException {
        TypeReference<Map<String, String>> typeReference = new TypeReference<Map<String, String>>() {};
        Map<String, String> chatMessageMap = objectMapper.readValue(chatMessage, typeReference);
        return chatMessageMap;
    }
    
    private ChatMessage convertStringChatMessageToChatMessage(String chatMessage) throws IOException {
        TypeReference<Map<String, String>> typeReference = new TypeReference<Map<String, String>>() {};
        Map<String, String> chatMessageMap = objectMapper.readValue(chatMessage, typeReference);
        return convertMapToChatMessage(chatMessageMap);
    }
    
    private ChatMessage convertMapToChatMessage(Map<String, String> chatMessageMap) {
    
        Instant instant = Instant.parse(chatMessageMap.get("date"));
        
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomId(Long.valueOf(chatMessageMap.get("chatRoomId")));
        chatMessage.setUserId(Long.valueOf(chatMessageMap.get("userId")));
        chatMessage.setRegDate(Date.from(instant));
        chatMessage.setNickname(chatMessageMap.get("nickname"));
        chatMessage.setContent(chatMessageMap.get("content"));
        
        return chatMessage;
    }
    
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("error!!!", exception);
        disconnectors.add(session.getId());
        users.remove(session.getId());
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} is disconnect", session.getId());
        disconnectors.add(session.getId());
        users.remove(session.getId());
    }
    
    
}
