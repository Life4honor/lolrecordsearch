package com.lolsearch.lolrecordsearch.repository;

import com.lolsearch.lolrecordsearch.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EntityManager entityManager;
    
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
    
    @Test(expected = DataIntegrityViolationException.class)
    public void testSaveDuplicateEmail() {
        User user = createTestUser();
        userRepository.save(user);
        
        User newUser = createTestUser();
        newUser.setSummoner("a");
        newUser.setNickname("a");
    
        userRepository.save(newUser);
    }
    
    @Test(expected = DataIntegrityViolationException.class)
    public void testSaveDuplicateNickname() {
        User user = createTestUser();
        userRepository.save(user);
    
        User newUser = createTestUser();
        newUser.setSummoner("a");
        newUser.setEmail("a");
    
        userRepository.save(newUser);
    }
    
    @Test(expected = DataIntegrityViolationException.class)
    public void testSaveDuplicateSummoner() {
        User user = createTestUser();
        userRepository.save(user);
    
        User newUser = createTestUser();
        newUser.setEmail("a");
        newUser.setNickname("a");
    
        userRepository.save(newUser);
    }
    
    @Test
    public void testFindByEmail() {
        User testUser = createTestUser();
        userRepository.save(testUser);
    
        User findUser = userRepository.findByEmail(testUser.getEmail());
    
        assertThat(findUser.getEmail()).isEqualTo(testUser.getEmail());
        
        
    }
    
    @Test
    public void testCountEmail() {
        User testUser = createTestUser();
    
        userRepository.save(testUser);
    
        long count = userRepository.countEmail(testUser.getEmail());
    
        assertThat(count).isEqualTo(1);
    }
    
    @Test
    public void testCountNickname() {
        User testUser = createTestUser();
    
        userRepository.save(testUser);
    
        long count = userRepository.countNickname(testUser.getNickname());
    
        assertThat(count).isEqualTo(1);
    }
    
    @Test
    public void testCountSummoner() {
        User testUser = createTestUser();
    
        userRepository.save(testUser);
        
        long count = userRepository.countSummoner(testUser.getSummoner());
        
        assertThat(count).isEqualTo(1);
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
        user.setNickname("짱짱맨");
        user.setSummoner("잘한다");
        return user;
    }
    
    
}