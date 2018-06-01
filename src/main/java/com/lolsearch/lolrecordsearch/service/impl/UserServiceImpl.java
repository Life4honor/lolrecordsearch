package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.*;
import com.lolsearch.lolrecordsearch.dto.UserInfo;
import com.lolsearch.lolrecordsearch.repository.RoleRepository;
import com.lolsearch.lolrecordsearch.repository.UserRepository;
import com.lolsearch.lolrecordsearch.repository.UserStateRepository;
import com.lolsearch.lolrecordsearch.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.*;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    
    @Override
    public Optional<String> findUserEmail(String nickname) {
        
        Optional<User> optionalUser = userRepository.findUserByNickname(nickname);
        String email = null;
        if(optionalUser.isPresent()) {
            email = optionalUser.get().getEmail();
        }
        
        return Optional.ofNullable(email);
    }
    
    @Transactional
    @Override
    public Optional<String> findUserPassword(String email, String nickname) {
    
        Optional<User> optionalUser = userRepository.findUserByEmailAndNickname(email, nickname);
        
        String password = null;
        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            password = UUID.randomUUID().toString().replace("-", "").substring(0, 5);
            PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
        }
    
        return Optional.ofNullable(password);
    }
    
    @Override
    public User findUser(Long id) {
    
        return userRepository.findById(id).get();
    }
    
    @Transactional
    @Override
    public long modifyNickname(Long id, String nickname) {
    
        return userRepository.updateUserNickname(id, nickname);
    }
    
    @Transactional
    @Override
    public long modifySummoner(Long id, String summoner) {
    
        return userRepository.updateUserSummoner(id, summoner);
    }
    
    @Transactional
    @Override
    public long modifyPassword(Long id, UserInfo userInfo) {
    
        User user = userRepository.findById(id).get();
        
        // 비밀번호 일치여부 확인
        boolean match = isMatchPassword(user.getPassword(), userInfo.getOldPassword());
        if(!match) {
            return 0;
        }
        
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    
        String encodedPassword = encoder.encode(userInfo.getPassword());
        
        return userRepository.updateUserPassword(id, encodedPassword);
    }
    
    private boolean isMatchPassword(String encodedPassword, String rawPassword) {
    
        String encodingId = extractEncodingId(encodedPassword);
        PasswordEncoder encoder = createDelegatingPasswordEncoder(encodingId);
        
        return encoder.matches(rawPassword, encodedPassword);
    }
    
    private String extractEncodingId(String encodedPassword) {
        Pattern pattern = Pattern.compile("\\{.*?\\}");
        Matcher matcher = pattern.matcher(encodedPassword);
        if(matcher.find()) {
            String group = matcher.group();
            String encodingId = group.substring(1, group.length()-1);
            return encodingId;
        }
        return null;
    }
    
    private PasswordEncoder createDelegatingPasswordEncoder(String encodingId) {
    
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", new BCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());
    
        return new DelegatingPasswordEncoder(encodingId, encoders);
    }
    
    @Override
    public boolean withdrawUser(Long id) {
    
        UserState withdrawState = userStateRepository.findByName(UserStatus.WITHDRAW);
    
        long count = userRepository.updateUserState(id, withdrawState);
        
        return count == 1;
    }
}
