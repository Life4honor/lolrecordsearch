package com.lolsearch.lolrecordsearch.websocket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearch.lolrecordsearch.domain.mongo.Chat;
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
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CustomTextWebSocketHandler extends TextWebSocketHandler {
    
    private static final int FIRST_USER_MESSAGE_SIZE = -20;
    private static final int DUPLICATE_CONNECTION_CODE = 4444;
    
    private final Map<Long, List<WebSocketSession>> subscriptionMap = new ConcurrentHashMap<>(); // 채팅방 별 구독자 리스트
    private final Map<String, Long> sessionChatRoomIdMap = new ConcurrentHashMap<>(); // 세션이 속한 방번호
    private final Set<String> disconnectors = new ConcurrentHashMap<>().newKeySet(); // 접속 종료한 세션 아이디
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private ChatService chatService;
    
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    
        Principal principal = session.getPrincipal();
        if(principal == null) {
            throw new IllegalStateException("로그인 해라!!");
        }
        
        Map<String, String> params = convertQueryStringToMap(session.getUri().getQuery());
        log.info("\n\n\nparams : {}\n\n\n", params);
        Long userId = getUserIdFromWebSocketSession(session);
        if(!isMatchUserId(userId, params)) {
            throw new IllegalArgumentException("이상한 놈");
        }
        
        Long chatRoomId = Long.valueOf(params.get("chatRoomId"));
    
//        if(isDuplicateUser(chatRoomId, userId)) {
//            // 이미 사용자 있으면 현재 연결 종료.
//            session.close(new CloseStatus(DUPLICATE_CONNECTION_CODE));
//            return;
//        }
        
        //클라이언트에서 전송한 채팅방 아이디, 세션 담기
        subscribe(chatRoomId, session);
        // 최초 접속 유저 몽고디비에 추가.
        chatService.pushUserId(chatRoomId, userId);
        
        ChatMessage chatMessage = makeConnectUserChatMessage(params);
        // 최초 접속 유저 접속 메시지 몽고디비에 저장
        Chat chat = chatService.saveChatMessage(chatRoomId, chatMessage);
        Optional<String> firstUserSessionId = Optional.of(session.getId());
        // 나머지 유저들한테 접속메시지 전송
        broadCastMessage(chatRoomId, firstUserSessionId, chat.getChatMessages());
        
        // 최초 접속 유저 한테만 20개 조회해서 전송
        List<ChatMessage> chatMessages = chatService.findChatMessages(chatMessage.getChatRoomId(), FIRST_USER_MESSAGE_SIZE);
        sendMessage(session, chatMessages);
    }
    
    private boolean isDuplicateUser(Long chatRoomId, Long userId) {
        List<WebSocketSession> sessionList = subscriptionMap.get(chatRoomId);
        if(sessionList == null) return false;
        for (WebSocketSession subscriber : sessionList) {
            if(isMatchUserId(userId, subscriber)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isMatchUserId(Long userId, WebSocketSession session) {
        Long sessionUserId = getUserIdFromWebSocketSession(session);
        return Long.compare(userId, sessionUserId) == 0;
    }
    
    private boolean isMatchUserId(Long userId, Map<String, String> params) {
        return Long.compare(userId, Long.valueOf(params.get("userId"))) == 0;
    }
    
    private Map<String, String> convertQueryStringToMap(String queryString) {
        return Arrays.stream(queryString.split("&"))
                    .map(s -> s.split("="))
                    .collect(Collectors.toMap(array -> array[0], array -> array[1]));
    }
    
    private void subscribe(Long chatRoomId, WebSocketSession session) {
        subscriptionMap.compute(chatRoomId, (k, list) -> {
            if(list == null) list = new CopyOnWriteArrayList<>();
            
            list.add(session);
            return list;
        });
        sessionChatRoomIdMap.put(session.getId(), chatRoomId);
    }
    
    private ChatMessage makeConnectUserChatMessage(Map<String, String> params) {
        
        Long chatRoomId = Long.valueOf(params.get("chatRoomId"));
        Long userId = Long.valueOf(params.get("userId"));
        String nickname = params.get("nickname");
        Date date = new Date(Long.valueOf(params.get("date")));
        
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomId(chatRoomId);
        chatMessage.setUserId(userId);
        chatMessage.setRegDate(date);
        chatMessage.setNickname(nickname);
        chatMessage.setContent(nickname+" 입장!!!!");
        
        return chatMessage;
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        
        Map<String, String> chatMessageMap = convertStringMessageToMap(message.getPayload());
        
        ChatMessage chatMessage = convertMapToChatMessage(chatMessageMap);
    
        Long userId = getUserIdFromWebSocketSession(session);
        if(!userId.equals(chatMessage.getUserId())) {
            throw new IllegalArgumentException("유효하지 않은 사용자 입니다.");
        }
        
        // 몽고디비에 메시지 저장 후 업데이트값 전송
        Chat chat = chatService.saveChatMessage(chatMessage.getChatRoomId(), chatMessage);
        
        broadCastMessage(chatMessage.getChatRoomId(), Optional.empty(), chat.getChatMessages());
    }
    
    private Long getUserIdFromWebSocketSession(WebSocketSession session) {
        return (Long) session.getAttributes().get("userId");
    }
    
    private void sendMessage(WebSocketSession session, List<ChatMessage> chatMessages) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsBytes(chatMessages));
        session.sendMessage(textMessage);
    }
    
    private boolean isFirstUser(Map<String, String> chatMessageMap) {
        return chatMessageMap.get("isFirst") != null;
    }
    
    private void broadCastMessage(Long chatRoomId, Optional<String> firstUserSessionId, List<ChatMessage> chatMessages) throws IOException {
        List<WebSocketSession> subscriberSessions = subscriptionMap.get(chatRoomId);
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
        unSubscribe(session);
    }
    
    private void unSubscribe(WebSocketSession session) {
        Long chatRoomId = sessionChatRoomIdMap.get(session.getId());
        Long userId = getUserIdFromWebSocketSession(session);
        chatService.deleteUserId(chatRoomId, userId);
        disconnectors.add(session.getId());
        sessionChatRoomIdMap.remove(session.getId());
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} is disconnect", session.getId());
//        if(status.getCode() == DUPLICATE_CONNECTION_CODE) {
//            log.info("{} 중복 로그인함!!!", session.getId());
//        }
        unSubscribe(session);
    }
    
    
}
