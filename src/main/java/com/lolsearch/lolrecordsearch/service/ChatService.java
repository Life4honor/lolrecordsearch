package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.mongo.Chat;
import com.lolsearch.lolrecordsearch.dto.ChatMessage;

import java.util.List;

public interface ChatService {

    Long createChatRoom(Long userId, String title);
    
    Chat saveChatMessage(Long chatRoomId, Long userId, ChatMessage chatMessage);
    
    List<ChatMessage> findChatMessages(Long chatRoomId, int size);

}
