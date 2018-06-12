package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.ChatRoom;
import com.lolsearch.lolrecordsearch.domain.jpa.User;
import com.lolsearch.lolrecordsearch.domain.jpa.UserChatRoom;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserChatRoomRepositoryTest {
    
    @Autowired
    private UserChatRoomRepository userChatRoomRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EntityManager entityManager;
    
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
    
    @Test
    public void testFindByUserIdWithPageable() {
        final int size1 = 10;
        final int size2 = 5;
        final long count = userChatRoomRepository.count();
        
        User user1 = new User();
        user1.setEmail("email1");
        User save1 = userRepository.save(user1);
    
        List<UserChatRoom> testUserChatRooms1 = createTestUserChatRoomList(size1, save1);
        
        userChatRoomRepository.saveAll(testUserChatRooms1);
        entityManager.flush();
        
        User user2 = new User();
        user2.setEmail("email2");
        User save2 = userRepository.save(user2);
    
        List<UserChatRoom> testUserChatRooms2 = createTestUserChatRoomList(size2, save2);
        
        userChatRoomRepository.saveAll(testUserChatRooms2);
        entityManager.flush();
        
        final int pageSize = 8;
        Sort sort = Sort.by(Sort.Order.desc("id"));
        PageRequest pageable = PageRequest.of(0, pageSize, sort);
    
        Page<UserChatRoom> userChatRooms = userChatRoomRepository.findByUserId(save1.getId(), null, pageable);
        long totalElements = userChatRooms.getTotalElements();
        assertThat(totalElements).isEqualTo(size1);
        assertThat(userChatRooms.getContent().size()).isEqualTo(pageSize);
    }
    
    @Test
    public void testFindUserChatRoom() {
        User user = new User();
        user.setEmail("email");
    
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTitle("title");
    
        UserChatRoom userChatRoom = new UserChatRoom();
        userChatRoom.setUser(user);
        userChatRoom.setChatRoom(chatRoom);
    
        UserChatRoom saveUserChatRoom = userChatRoomRepository.save(userChatRoom);
    
        User saveUser = saveUserChatRoom.getUser();
        ChatRoom saveChatRoom = saveUserChatRoom.getChatRoom();
    
        Optional<UserChatRoom> findUserChatRoom = userChatRoomRepository.findUserChatRoom(saveChatRoom.getId(), saveUser.getId());
        assertThat(findUserChatRoom.isPresent()).isTrue();
        assertThat(findUserChatRoom.get()).isEqualTo(saveUserChatRoom);
    }
    
    private List<UserChatRoom> createTestUserChatRoomList(int size, User user) {
        List<UserChatRoom> list = new ArrayList<>();
        
        for(int i = 1; i <= size; i++) {
            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setTitle(""+i);
            UserChatRoom userChatRoom = new UserChatRoom();
            userChatRoom.setUser(user);
            userChatRoom.setChatRoom(chatRoom);
            list.add(userChatRoom);
        }
        
        return list;
    }
    
    
}