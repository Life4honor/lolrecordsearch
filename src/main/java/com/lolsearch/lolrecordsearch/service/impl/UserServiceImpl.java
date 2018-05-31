package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.User;
import com.lolsearch.lolrecordsearch.dto.UserInfo;
import com.lolsearch.lolrecordsearch.repository.UserRepository;
import com.lolsearch.lolrecordsearch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public User registUser(UserInfo userInfo) {
        return null;
    }
    
    @Override
    public boolean isExistEmail(String email) {
    
        long count = userRepository.countEmail(email);
        
        return count > 0;
    }
    
    @Override
    public boolean isExistNickname(String nickname) {
    
        long count = userRepository.countNickname(nickname);
        
        return count > 0;
    }
    
    @Override
    public boolean isExistSummoner(String summoner) {
    
        long count = userRepository.countSummoner(summoner);
        
        return count > 0;
    }
    
}
