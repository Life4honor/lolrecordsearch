package com.lolsearch.lolrecordsearch.repository.redis;

import com.lolsearch.lolrecordsearch.repository.redis.impl.RedisRepositoryImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataRedisTest
public class RedisRepositoryTest {
    
    private static final Long CHAT_ROOM_ID = Long.MAX_VALUE;
    
    @TestConfiguration
    public static class RedisRepositoryTestConfig {
        
        @Bean
        public RedisRepository redisRepository() {
            return new RedisRepositoryImpl();
        }
        
    }
    
    @Autowired
    private RedisRepository redisRepository;

    @Before
    public void setUp() {
        redisRepository.deleteChatRoom(CHAT_ROOM_ID);
    }
    
    @After
    public void tearDown() {
        redisRepository.deleteChatRoom(CHAT_ROOM_ID);
    }
    
    @Test
    public void testNotNull() {
        assertThat(redisRepository).isNotNull();
    }
    
    @Test
    public void addChatRoomUser() {
        final Long userId = 1L;
        Long changed = redisRepository.addChatRoomUser(CHAT_ROOM_ID, userId);
        assertThat(changed).isNotEqualTo(0);
        
    }
    
    @Test
    public void findChatRoomUsers() {
        final Long userId1 = 1L;
        final Long userId2 = 2L;
        final Long userId3 = 3L;
        Long changed = redisRepository.addChatRoomUser(CHAT_ROOM_ID, userId1);
        changed = redisRepository.addChatRoomUser(CHAT_ROOM_ID, userId2);
        changed = redisRepository.addChatRoomUser(CHAT_ROOM_ID, userId3);
        assertThat(changed).isNotNull();
        
        Set<String> users = redisRepository.findChatRoomUsers(CHAT_ROOM_ID);
        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(3);
    
        
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(users);
        System.out.println();
        System.out.println();
        System.out.println();
    }
    
    @Test
    public void testRemoveChatRoomUser() {
    
        final Long userId1 = 1L;
        final Long userId2 = 2L;
        final Long userId3 = 3L;
        
        redisRepository.addChatRoomUser(CHAT_ROOM_ID, userId1);
        redisRepository.addChatRoomUser(CHAT_ROOM_ID, userId2);
        redisRepository.addChatRoomUser(CHAT_ROOM_ID, userId3);
        
        Set<String> users = redisRepository.findChatRoomUsers(CHAT_ROOM_ID);
        assertThat(users.size()).isEqualTo(3);
    
        redisRepository.removeChatRoomUser(CHAT_ROOM_ID, userId1);
        users = redisRepository.findChatRoomUsers(CHAT_ROOM_ID);
        assertThat(users.size()).isEqualTo(2);
        
    }
    
    @Test
    public void testIsChatRoomMember() {
    
        final Long userId1 = 1L;
        final Long userId2 = 2L;
        final Long userId3 = 3L;
    
        redisRepository.addChatRoomUser(CHAT_ROOM_ID, userId1);
        redisRepository.addChatRoomUser(CHAT_ROOM_ID, userId2);
        redisRepository.addChatRoomUser(CHAT_ROOM_ID, userId3);
    
        redisRepository.removeChatRoomUser(CHAT_ROOM_ID, userId1);
        
        Boolean isMember = redisRepository.isChatRoomMember(CHAT_ROOM_ID, userId1);
        assertThat(isMember).isFalse();
        isMember = redisRepository.isChatRoomMember(CHAT_ROOM_ID, userId2);
        assertThat(isMember).isTrue();
    }
    
}