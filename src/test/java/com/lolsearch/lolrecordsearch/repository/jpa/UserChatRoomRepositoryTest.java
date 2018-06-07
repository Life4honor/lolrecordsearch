package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.ChatRoom;
import com.lolsearch.lolrecordsearch.domain.jpa.User;
import com.lolsearch.lolrecordsearch.domain.jpa.UserChatRoom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserChatRoomRepositoryTest {
    
    @Autowired
    private UserChatRoomRepository userChatRoomRepository;
    
    @Test
    public void testNotNull() {
        assertThat(userChatRoomRepository).isNotNull();
    }
    
    @Test
    public void testSaveUserChatRoom() {
        User user = new User();
        user.setEmail("email");
    
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTitle("testRoom");
    
        UserChatRoom userChatRoom = new UserChatRoom();
        userChatRoom.setChatRoom(chatRoom);
        userChatRoom.setUser(user);
    
        UserChatRoom save = userChatRoomRepository.save(userChatRoom);
        assertThat(save).isNotNull();
    }
    
    @Test
    public void testFindByUserId() {
    
        User user = new User();
        user.setEmail("email");
    
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTitle("testRoom");
    
        UserChatRoom userChatRoom = new UserChatRoom();
        userChatRoom.setChatRoom(chatRoom);
        userChatRoom.setUser(user);
    
        UserChatRoom save = userChatRoomRepository.save(userChatRoom);
    
        List<UserChatRoom> chatRooms = userChatRoomRepository.findByUserId(save.getUser().getId());
    
        assertThat(chatRooms.get(0)).isEqualTo(save);
    }
    
}