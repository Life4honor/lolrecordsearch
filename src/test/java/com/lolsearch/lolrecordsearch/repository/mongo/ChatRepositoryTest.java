package com.lolsearch.lolrecordsearch.repository.mongo;

import com.lolsearch.lolrecordsearch.domain.mongo.Chat;
import com.lolsearch.lolrecordsearch.dto.ChatMessage;
import com.mongodb.client.result.UpdateResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataMongoTest
public class ChatRepositoryTest {
    
    private static final AtomicLong chatRoomIdCreator = new AtomicLong();
    
    @Autowired
    private ChatRepository chatRepository;
    
    @Test
    public void testNotNull() {
        assertThat(chatRepository).isNotNull();
    }
    
    @Test
    public void testSaveChat() {
        Long chatRoomId = chatRoomIdCreator.incrementAndGet();
    
        Chat chat = new Chat();
        chat.setChatRoomId(chatRoomId);
    
        Mono<Chat> chatMono = chatRepository.save(chat);
        Chat save = chatMono.block();
    
        assertThat(save).isNotNull();
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        System.out.println(save);
//        System.out.println();
//        System.out.println();
//        System.out.println();
    }
    
    @Test
    public void testPushUserId() {
        Long chatRoomId = chatRoomIdCreator.incrementAndGet();
    
        Chat chat = new Chat();
        chat.setChatRoomId(chatRoomId);
        Mono<Chat> save = chatRepository.save(chat);
        save.block();
        
        final Long userId1 = 1L;
        final Long userId2 = 2L;
    
        long modified = chatRepository.pushUserId(chatRoomId, userId1);
        assertThat(modified).isEqualTo(1);
    
        modified = chatRepository.pushUserId(chatRoomId, userId2);
        assertThat(modified).isEqualTo(1);
    
        modified = chatRepository.pushUserId(chatRoomId, userId1);
        assertThat(modified).isEqualTo(0);
    }
    
    @Test
    public void testReactivePushUserId() {
        Long chatRoomId = chatRoomIdCreator.incrementAndGet();
    
        Chat chat = new Chat();
        chat.setChatRoomId(chatRoomId);
        Mono<Chat> save = chatRepository.save(chat);
        save.block();
    
        final Long userId1 = 1L;
        final Long userId2 = 2L;
        UpdateResult updateResult1 = chatRepository.reactivePushUserId(chatRoomId, userId1).block();
        UpdateResult updateResult2 = chatRepository.reactivePushUserId(chatRoomId, userId2).block();
        
        assertThat(updateResult1.getModifiedCount()).isEqualTo(1);
    
        Optional<Chat> optionalChat = chatRepository.findByChatRoomId(chatRoomId).blockOptional();
        
        assertThat(optionalChat.isPresent()).isTrue();
        assertThat(optionalChat.get().getUsers().size()).isEqualTo(2);
    }
    
    @Test
    public void testPushChatMessage() {
        Long chatRoomId = chatRoomIdCreator.incrementAndGet();
    
        Chat chat = new Chat();
        chat.setChatRoomId(chatRoomId);
        Mono<Chat> save = chatRepository.save(chat);
        save.block();
    
        Long userId1 = 1L;
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoomId(chatRoomId);
        chatMessage.setContent("채팅이다!!");
        chatMessage.setNickname("닉네임");
        chatMessage.setRegDate(new Date());
        chatMessage.setUserId(userId1);
        int messageSize = 0;
        Chat pushChat = chatRepository.pushChatMessage(chatRoomId, chatMessage);
        messageSize++;
        assertThat(pushChat.getChatMessages().size()).isEqualTo(1);
        
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        System.out.println(pushChat);
//        System.out.println();
//        System.out.println();
//        System.out.println();
        
        Long userId2 = 2L;
        chatMessage.setUserId(userId2);
        pushChat = chatRepository.pushChatMessage(chatRoomId, chatMessage);
        messageSize++;
        assertThat(pushChat.getChatMessages().size()).isEqualTo(messageSize);
        
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        System.out.println(pushChat);
//        System.out.println();
//        System.out.println();
//        System.out.println();
    }
    
    @Test
    public void testFindByChatRoomId() {
        Long chatRoomId = chatRoomIdCreator.incrementAndGet();
    
        Chat chat = new Chat();
        chat.setChatRoomId(chatRoomId);
        Chat save = chatRepository.save(chat).block();
    
        Optional<Chat> optionalChat = chatRepository.findByChatRoomId(chatRoomId).blockOptional();
        assertThat(optionalChat.isPresent()).isTrue();
        assertThat(optionalChat.get()).isEqualTo(save);
    }
    
    @Test
    public void testPullChatUser() {
        Long chatRoomId = chatRoomIdCreator.incrementAndGet();
    
        Chat chat = new Chat();
        chat.setChatRoomId(chatRoomId);
        Chat save = chatRepository.save(chat).block();
    
        final Long userId1 = 1L;
        final Long userId2 = 2L;
        chatRepository.pushUserId(chatRoomId, userId1);
        chatRepository.pushUserId(chatRoomId, userId2);
    
        Optional<Chat> optionalChat1 = chatRepository.findByChatRoomId(chatRoomId).blockOptional();
        
        assertThat(optionalChat1.get().getUsers().size()).isEqualTo(2);
        
        chatRepository.pullChatUser(chatRoomId, userId1);
    
        Optional<Chat> optionalChat2 = chatRepository.findByChatRoomId(chatRoomId).blockOptional();
    
        Set<Long> users = optionalChat2.get().getUsers();
        assertThat(users.contains(userId1)).isFalse();
    
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        System.out.println(optionalChat.get());
//        System.out.println();
//        System.out.println();
//        System.out.println();
    }
    
    @Test
    public void testDeleteByChatRoomId() {
        Long chatRoomId = chatRoomIdCreator.incrementAndGet();
    
        Chat chat = new Chat();
        chat.setChatRoomId(chatRoomId);
        Chat save = chatRepository.save(chat).block();
        
        chatRepository.deleteByChatRoomId(chatRoomId).block();
    
        Optional<Chat> optionalChat = chatRepository.findByChatRoomId(chatRoomId).blockOptional();
        assertThat(optionalChat.isPresent()).isFalse();
        
    }
    
    @Test
    public void testFindByChatRoomIdWithChatMassegeLimit() {
        Long chatRoomId = chatRoomIdCreator.incrementAndGet();
    
        Chat chat = new Chat();
        chat.setChatRoomId(chatRoomId);
        Chat save = chatRepository.save(chat).block();
    
        List<ChatMessage> chatMessageList = createChatMessageList(chatRoomId, 10);
        chatMessageList.forEach(m -> chatRepository.pushChatMessage(chatRoomId, m));
        System.out.println("==============================================================");
        System.out.println("넣기전!!!");
        Chat findChat = chatRepository.findByChatRoomIdWithChatMessageLimit(chatRoomId, -3);
        System.out.println("넣은 후");
        assertThat(findChat.getChatMessages().size()).isEqualTo(3);
        System.out.println("비교 후");
        System.out.println("==============================================================");
//        System.out.println();
//        System.out.println();
//        System.out.println();
//        System.out.println(findChat.getChatMessages());
//        System.out.println();
//        System.out.println();
//        System.out.println();
    }
    
    
    private List<ChatMessage> createChatMessageList(Long chatRoomId, int size) {
        List<ChatMessage> list = new ArrayList<>();
        for(int i = 1; i <= size; i++) {
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setChatRoomId(chatRoomId);
            chatMessage.setContent("내용"+i);
            chatMessage.setNickname("닉네임"+i);
            chatMessage.setRegDate(new Date());
            chatMessage.setUserId(Long.valueOf(i));
            list.add(chatMessage);
        }
        return list;
    }
    
    
    
}