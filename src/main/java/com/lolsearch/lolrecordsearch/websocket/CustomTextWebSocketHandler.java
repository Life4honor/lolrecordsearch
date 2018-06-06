package com.lolsearch.lolrecordsearch.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearch.lolrecordsearch.dto.ChatMessage;
import com.lolsearch.lolrecordsearch.jackson.LocalDateTimeDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.format.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.security.Principal;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CustomTextWebSocketHandler extends TextWebSocketHandler {
    
    // CopyOnWriteArrayList
    private final Map<Long, List<WebSocketSession>> subscribers = new ConcurrentHashMap<>();
    private final Map<String, WebSocketSession> users = new ConcurrentHashMap<>();
    private final Set<String> disconnectors = new ConcurrentHashMap<>().newKeySet();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Principal principal = session.getPrincipal();
        if(principal == null) {
            throw new IllegalStateException("로그인 해라!!");
        }
        
        log.info("============================== afterConnectionEstablished ===================================");
        log.info("sessionId : {}", session.getId());
        log.info("principal : {}", principal);
        if(principal != null) {
            log.info("principal className : {}", principal.getClass().getName());
            log.info("principal getName() : {}", principal.getName());
        }
        
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
        
        // 채팅방 최초 연결자 확인할 플레그 값 필요.
        
        // 최초 연결자는 몽고디비에서 메시지 최근순으로 20건까지 조회하여 전송. 기본갑 size = -1
        
        // 채팅방아이디, 사용자 닉네임, 채팅입력내용 전송 필요.
    
        log.info("message : {}", message.getPayload());
//        ChatMessage chatMessage = objectMapper.readValue(message.getPayload(), ChatMessage.class);
        ChatMessage chatMessage = convertStringChatMessageToChatMessage(message.getPayload());
        log.info("chatMessage : {}", chatMessage);
    
        // 몽고디비에 업데이트 후 조회하여 전송
        
        // paticipants에서 채팅방 아이디에 해당하는 세션아이디 리스트 꺼내온다.
        
        // 세션아이디 리스트반복 돌면서 subscriber에서 일치하는 세션에게만 메시지 전송
        List<WebSocketSession> subscriberSessions = subscribers.get(chatMessage.getChatRoomId());
        for(int i = 0; i < subscriberSessions.size(); i++) {
            WebSocketSession webSocketSession = subscriberSessions.get(i);
        // disconnectors에 세션아이디와 일치하는 아이디 있으면 리스트와 disconnectors에서 세션아이디 삭제.
            if(disconnectors.contains(webSocketSession.getId())) {
                subscriberSessions.remove(i);
                disconnectors.remove(webSocketSession.getId());
                continue;
            }
            TextMessage textMessage = new TextMessage(objectMapper.writeValueAsBytes(chatMessage));
            webSocketSession.sendMessage(textMessage);
        }
        
        
        // 전송 메시지는 리스트 형태로 전송필요. ==> 최초 접속자 때문에.
        
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
        disconnectors.add(session.getId());
        users.remove(session.getId());
    }
    
    
}
