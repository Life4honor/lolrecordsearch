package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.ChatRoom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@DataJpaTest
public class ChatRoomRepositoryTest {
    
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    
    @Test
    public void testNotNull() {
        assertThat(chatRoomRepository).isNotNull();
    }
    
    @Test
    public void testSaveChatRoom() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTitle("testRoom");
    
        ChatRoom save = chatRoomRepository.save(chatRoom);
        assertThat(save).isNotNull();
    }
    
    @Test
    public void testFindById() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTitle("testRoom");
        ChatRoom save = chatRoomRepository.save(chatRoom);
    
        ChatRoom findRoom = chatRoomRepository.findById(save.getId()).get();
        assertThat(findRoom).isEqualTo(save);
    }
    
    
    @Test
    public void testFindChatRooms() {
        long count = chatRoomRepository.count();
    
        final int chatRoomSize = 13;
        List<ChatRoom> testChatRooms = createChatRoomList(chatRoomSize);
    
        chatRoomRepository.saveAll(testChatRooms);
    
        Sort sort = Sort.by(Sort.Direction.DESC,"id");
        
        final int pageSize = 10;
        Pageable pageable = PageRequest.of(0, pageSize, sort);
    
        Page<ChatRoom> chatRoomPage = chatRoomRepository.findChatRooms(Optional.empty(), pageable);
        List<ChatRoom> chatRooms = chatRoomPage.getContent();
        
        assertThat(chatRoomPage.getTotalElements()).isEqualTo(chatRoomSize+count);
        assertThat(chatRooms.size()).isEqualTo(pageSize);
    }
    
    
    private List<ChatRoom> createChatRoomList(int size) {
        List<ChatRoom> list = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setTitle("testRoom"+i);
            list.add(chatRoom);
        }
        
        return list;
    }
    
    
}