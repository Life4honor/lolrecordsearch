package com.lolsearch.lolrecordsearch.repository;

import com.lolsearch.lolrecordsearch.domain.*;
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
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    public void testNotNull() {
        assertThat(userRepository).isNotNull();
    }
    
    @Test
    public void testSaveUser() {
        User user = createTestUser();
        User saveUser = userRepository.save(user);
    
        User findUser = userRepository.findById(saveUser.getId()).get();
    
        assertThat(user.getEmail()).isEqualTo(findUser.getEmail());
    }
    
    @Test
    public void testFindByEmail() {
        User testUser = createTestUser();
        userRepository.save(testUser);
    
        User findUser = userRepository.findByEmail(testUser.getEmail());
    
        assertThat(findUser.getEmail()).isEqualTo(testUser.getEmail());
        
        
    }
    
    private User createTestUser() {
        UserState userState = new UserState();
        userState.setName(UserStatus.NORMAL);
        
        Role role = new Role();
        role.setName(RoleName.USER);
        
        User user = new User();
        user.setUserState(userState);
        user.addRole(role);
        
        user.setEmail("test111@gmail.com");
        return user;
    }
    
    
}