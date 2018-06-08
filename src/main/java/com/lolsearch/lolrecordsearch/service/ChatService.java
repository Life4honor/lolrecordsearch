package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.jpa.ChatRoom;
import com.lolsearch.lolrecordsearch.domain.mongo.Chat;
import com.lolsearch.lolrecordsearch.dto.ChatMessage;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ChatService {

    Long createChatRoom(Long userId, String title);
    
    Optional<ChatRoom> findChatRoom(Long chatRoomId);
    
    long pushUserId(Long chatRoomId, Long userId);
    
    Chat saveChatMessage(Long chatRoomId, ChatMessage chatMessage);
    
    List<ChatMessage> findChatMessages(Long chatRoomId, int size);
    
    Page<ChatRoom> findChatRooms(int page, String title);
    
    long deleteUserId(Long chatRoomId, Long userId);

}
