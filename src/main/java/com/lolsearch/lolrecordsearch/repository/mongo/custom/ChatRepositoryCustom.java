package com.lolsearch.lolrecordsearch.repository.mongo.custom;

import com.lolsearch.lolrecordsearch.domain.mongo.Chat;
import com.lolsearch.lolrecordsearch.dto.ChatMessage;
import com.mongodb.client.result.UpdateResult;
import reactor.core.publisher.Mono;

public interface ChatRepositoryCustom {
    
    long pushUserId(Long chatRoomId, Long userId);
    
    Mono<UpdateResult> reactivePushUserId(Long chatRoomId, Long userId);
    
    Chat pushChatMessage(Long chatRoomId, ChatMessage message);
    
    Mono<Chat> reactivePushChatMessage(Long chatRoomId, ChatMessage message);
    
    long pullChatUser(Long chatRoomId, Long userId);
    
    Mono<UpdateResult> reactivePullChatUser(Long chatRoomId, Long userId);
    
    Chat findByChatRoomIdWithChatMessageLimit(Long chatRoomId, int size);
    
    Mono<Chat> reactiveFindByChatRoomIdWithChatMessageLimit(Long chatRoomId, int size);
}
