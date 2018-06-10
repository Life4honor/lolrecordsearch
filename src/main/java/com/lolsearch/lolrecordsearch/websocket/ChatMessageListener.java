package com.lolsearch.lolrecordsearch.websocket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearch.lolrecordsearch.dto.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class ChatMessageListener implements MessageListener {
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private CustomTextWebSocketHandler handler;
    
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = new String(message.getChannel());
        try {
            Long chatRoomId = Long.valueOf(channel.split("\\.")[1]);
            TypeReference<List<ChatMessage>> ref = new TypeReference<List<ChatMessage>>() {};
            List<ChatMessage> chatMessages = objectMapper.readValue(message.getBody(), ref);
            
            String firstUserSessionId = chatMessages.get(0).getFirstUserSessionId();
            
            handler.broadCastMessage(chatRoomId, Optional.ofNullable(firstUserSessionId), chatMessages);
        }
        catch (IOException e) {
            log.error("Json Convert Error", e);
        }
    }
}
