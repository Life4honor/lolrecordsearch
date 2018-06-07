package com.lolsearch.lolrecordsearch.security;

import com.lolsearch.lolrecordsearch.domain.jpa.User;
import com.lolsearch.lolrecordsearch.dto.LoginUser;
import com.lolsearch.lolrecordsearch.repository.jpa.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        User user = userRepository.findByEmail(email);
        
        if(user == null) {
            throw new UsernameNotFoundException("User Not Found");
        }
    
        LoginUser loginUser = makeLoginUser(user);
        
        return loginUser;
    }
    
    private LoginUser makeLoginUser(User user) {
        
        List<GrantedAuthority> authorities = user.getRoles()
                                                    .stream()
                                                    .map(r -> new SimpleGrantedAuthority(r.getName().getRoleName()))
                                                    .collect(Collectors.toList());
    
        boolean enabled = true;
        boolean accoundNonLocked = true;
        
        switch (user.getUserState().getName()) {
            case SUSPENSION:
                accoundNonLocked = false;
                break;
            case WITHDRAW:
                enabled = false;
                break;
        }
        
        return new LoginUser(user, enabled, accoundNonLocked, authorities);
    }
}
