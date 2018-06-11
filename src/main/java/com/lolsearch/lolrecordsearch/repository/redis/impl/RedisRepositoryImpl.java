package com.lolsearch.lolrecordsearch.repository.redis.impl;

import com.lolsearch.lolrecordsearch.repository.redis.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public class RedisRepositoryImpl implements RedisRepository {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Override
    public Set<String> findChatRoomUsers(Long chatRoomId) {
    
        return redisTemplate.opsForSet().members(makeChatRoomKey(chatRoomId));
    }
    
    @Override
    public Long addChatRoomUser(Long chatRoomId, Long userId) {
    
        return redisTemplate.opsForSet().add(makeChatRoomKey(chatRoomId), String.valueOf(userId));
    }
    
    @Override
    public Long removeChatRoomUser(Long chatRoomId, Long userId) {
    
        return redisTemplate.opsForSet().remove(makeChatRoomKey(chatRoomId), String.valueOf(userId));
    }
    
    private String makeChatRoomKey(Long chatRoomId) {
    
        return "chatRoom:" + chatRoomId;
    }
    
    @Override
    public Boolean isChatRoomMember(Long chatRoomId, Long userId) {
    
        return redisTemplate.opsForSet().isMember(makeChatRoomKey(chatRoomId), String.valueOf(userId));
    }
    
    @Override
    public Boolean deleteChatRoom(Long chatRoomId) {
        
        return redisTemplate.delete(makeChatRoomKey(chatRoomId));
    }
    
}
