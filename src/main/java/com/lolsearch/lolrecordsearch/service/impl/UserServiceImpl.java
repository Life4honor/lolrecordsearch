package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.*;
import com.lolsearch.lolrecordsearch.dto.UserInfo;
import com.lolsearch.lolrecordsearch.repository.RoleRepository;
import com.lolsearch.lolrecordsearch.repository.UserRepository;
import com.lolsearch.lolrecordsearch.repository.UserStateRepository;
import com.lolsearch.lolrecordsearch.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserStateRepository userStateRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Transactional
    @Override
    public User registUser(UserInfo userInfo) {
        User user = new User();
        BeanUtils.copyProperties(userInfo, user);
    
        UserState userState = userStateRepository.findByName(UserStatus.NORMAL);
        user.setUserState(userState);
        
        Role role = roleRepository.findByName(RoleName.USER);
        user.addRole(role);
    
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String encodedPasword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPasword);
    
        return userRepository.save(user);
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
