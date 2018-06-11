package com.lolsearch.lolrecordsearch.repository.redis;


import java.util.Set;

public interface RedisRepository {
    
    Set<String> findChatRoomUsers(Long chatRoomId);
    
    Long addChatRoomUser(Long chatRoomId, Long userId);
    
    Long removeChatRoomUser(Long chatRoomId, Long userId);

    Boolean isChatRoomMember(Long chatRoomId, Long userId);
    
    Boolean deleteChatRoom(Long chatRoomId);
    
}
