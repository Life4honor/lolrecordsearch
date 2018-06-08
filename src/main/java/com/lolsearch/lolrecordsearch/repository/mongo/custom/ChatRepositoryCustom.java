package com.lolsearch.lolrecordsearch.repository.mongo.custom;

import com.lolsearch.lolrecordsearch.domain.mongo.Chat;
import com.lolsearch.lolrecordsearch.dto.ChatMessage;

public interface ChatRepositoryCustom {
    
    long pushUserId(Long chatRoomId, Long userId);
    
    Chat pushChatMessage(Long chatRoomId, ChatMessage message);
    
    long pullChatUser(Long chatRoomId, Long userId);
    
    Chat findByChatRoomIdWithChatMessageLimit(Long chatRoomId, int size);
    
}
