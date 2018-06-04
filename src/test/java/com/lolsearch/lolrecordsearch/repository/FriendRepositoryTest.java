package com.lolsearch.lolrecordsearch.repository;


import com.lolsearch.lolrecordsearch.domain.Friend;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@DataJpaTest
public class FriendRepositoryTest {
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private FriendRepository friendRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void testNotNull() {
        assertThat(friendRepository).isNotNull();
    }
    
    @Test
    public void testFindFriendsByUserId() {
        final int size = 5;
        Pageable pageable = PageRequest.of(0, size);
        
        Page<Friend> page = friendRepository.findFriendsByUserId(1L, null, pageable);
    
        List<Friend> friends = page.getContent();
        assertThat(friends.size()).isEqualTo(size);
        
    }
    
    private List<Friend> createTestFriends(int count) {
        List<Friend> friends = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Friend friend = new Friend();
//            friend.setUser(user);
            friend.setSummoner(String.valueOf(i));
            friends.add(friend);
        }
        return friends;
    }
    
}