package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.repository.UserRepository;
import com.lolsearch.lolrecordsearch.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
public class UserServiceImplTest {
    
    @TestConfiguration
    static class UserSerivceImplContextConfig {
        
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
        
    }
    
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;
    
    @Test
    public void testNotNull() {
        assertThat(userService).isNotNull();
    }
    
    @Test
    public void testIsExistEmail() {
        
        String email1 = "test@test.com";
        String email2 = "test1@test.com";
        when(userRepository.countEmail(email1)).thenReturn(1L);
        when(userRepository.countEmail(email2)).thenReturn(0L);
        
        boolean existEmail = userService.isExistEmail(email1);
        assertThat(existEmail).isTrue();
        
        existEmail = userService.isExistEmail(email2);
        assertThat(existEmail).isFalse();
    }
    
    
}