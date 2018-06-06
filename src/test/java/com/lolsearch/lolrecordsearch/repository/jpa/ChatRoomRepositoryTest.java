package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.ChatRoom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
    
}