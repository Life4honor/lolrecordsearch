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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


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
        given(userRepository.countEmail(email1)).willReturn(1L);
        given(userRepository.countEmail(email2)).willReturn(0L);
        
        boolean existEmail1 = userService.isExistEmail(email1);
        assertThat(existEmail1).isTrue();
        
        boolean existEmail2 = userService.isExistEmail(email2);
        assertThat(existEmail2).isFalse();
    }
    
    @Test
    public void testIsExistNickname() {
        String nickname1 = "하하하";
        String nickname2 = "호호호";
        given(userRepository.countNickname(nickname1)).willReturn(1L);
        given(userRepository.countNickname(nickname2)).willReturn(0L);
    
        boolean existNickname1 = userService.isExistNickname(nickname1);
        assertThat(existNickname1).isTrue();
    
        boolean existNickname2 = userService.isExistNickname(nickname2);
        assertThat(existNickname2).isFalse();
        
    }
    
    @Test
    public void testIsExistSummoner() {
        String summoner1 = "summoner1";
        String summoner2 = "summoner2";
        given(userRepository.countSummoner(summoner1)).willReturn(1L);
        given(userRepository.countSummoner(summoner2)).willReturn(0L);
    
        boolean existSummoner1 = userService.isExistSummoner(summoner1);
        assertThat(existSummoner1).isTrue();
    
        boolean existSummoner2 = userService.isExistSummoner(summoner2);
        assertThat(existSummoner2).isFalse();
    }
    
    
}