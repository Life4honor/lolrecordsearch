package com.lolsearch.lolrecordsearch.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearch.lolrecordsearch.config.RedisConfig;
import com.lolsearch.lolrecordsearch.dto.ChatMessage;
import com.lolsearch.lolrecordsearch.repository.redis.RedisRepository;
import com.lolsearch.lolrecordsearch.service.ChatService;
import com.mongodb.client.result.UpdateResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private ChatService chatService;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Autowired
    private RedisRepository redisRepository;
    
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Principal principal = session.getPrincipal();
        if(principal == null) {
            throw new IllegalStateException("로그인 해라!!");
        }
    
        Map<String, String> params = convertQueryStringToMap(session.getUri().getQuery());
        
        Long userId = getUserIdFromWebSocketSession(session);
        if(!isMatchUserId(userId, params)) {
            throw new IllegalArgumentException("비정상적인 접근입니다.");
        }
        
        Long chatRoomId = Long.valueOf(params.get("chatRoomId"));
        Boolean isMember = redisRepository.isChatRoomMember(chatRoomId, userId);
        if(isMember) {
            session.close(new CloseStatus(DUPLICATE_CONNECTION_CODE, userId+" 중복 접속!!"));
            return;
        }
        redisRepository.addChatRoomUser(chatRoomId, userId);
        //클라이언트에서 전송한 채팅방 아이디, 세션 담기
        subscribe(chatRoomId, session);
        // 최초 접속 유저 몽고디비에 추가.
        UpdateResult updateResult = chatService.reactivePushUserId(chatRoomId, userId).block();
        if(updateResult.getModifiedCount() == 1) {
            ChatMessage chatMessage = makeConnectUserChatMessage(params);
            chatMessage.setFirstUserSessionId(session.getId());
            publishToRedis(chatMessage);
            // 최초 접속 유저 접속 메시지 몽고디비에 저장
            chatService.reactiveSaveChatMessage(chatRoomId, chatMessage).block();
        }
        
        // 최초 접속 유저 한테만 20개 조회해서 전송
        List<ChatMessage> chatMessages = chatService.findChatMessages(chatRoomId, FIRST_USER_MESSAGE_SIZE);
        sendMessage(session, chatMessages);
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
    
    private void publishToRedis(ChatMessage chatMessage) throws JsonProcessingException {
        String jsonStr = objectMapper.writeValueAsString(Arrays.asList(chatMessage));
        redisTemplate.convertAndSend(RedisConfig.REDIS_TOPIC+"."+chatMessage.getChatRoomId(), jsonStr);
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        
        Map<String, String> chatMessageMap = convertStringMessageToMap(message.getPayload());
        
        ChatMessage chatMessage = convertMapToChatMessage(chatMessageMap);
    
        Long userId = getUserIdFromWebSocketSession(session);
        if(!userId.equals(chatMessage.getUserId())) {
            throw new IllegalArgumentException("유효하지 않은 사용자 입니다.");
        }
        
        // 몽고디비에 메시지 저장
        chatService.reactiveSaveChatMessage(chatMessage.getChatRoomId(), chatMessage).subscribe();
    
        publishToRedis(chatMessage);
        
    }
    
    private Long getUserIdFromWebSocketSession(WebSocketSession session) {
        return (Long) session.getAttributes().get("userId");
    }
    
    private void sendMessage(WebSocketSession session, List<ChatMessage> chatMessages) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsBytes(chatMessages));
        session.sendMessage(textMessage);
    }
    
    public void broadCastMessage(Long chatRoomId, Optional<String> firstUserSessionId, List<ChatMessage> chatMessages) throws IOException {
        
        List<WebSocketSession> subscriberSessions = subscriptionMap.get(chatRoomId);
        for(int i = 0; i < subscriberSessions.size(); i++) {
            WebSocketSession webSocketSession = subscriberSessions.get(i);
        
            if(webSocketSession == null) {
                subscriberSessions.remove(i);
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
        
        redisRepository.removeChatRoomUser(chatRoomId, userId);
        removeSessionFromSubscriptionMap(chatRoomId, session.getId());
    
        sessionChatRoomIdMap.remove(session.getId());
    }
    
    private void removeSessionFromSubscriptionMap(Long chatRoomId, String sessionId) {
        if(chatRoomId == null) {
            return;
        }
        List<WebSocketSession> webSocketSessions = subscriptionMap.get(chatRoomId);
        
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
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} is disconnect", session.getId());
        if(status.getCode() == DUPLICATE_CONNECTION_CODE) {
            log.info("{} 중복 로그인함!!!", session.getId());
            return;
        }
        unSubscribe(session);
    }
    
    
}
