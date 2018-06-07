package com.lolsearch.lolrecordsearch.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class LoginUser extends User {
    
    private Long id;
    private String email;
    private String nickname;
    private String summoner;
    
    public LoginUser(com.lolsearch.lolrecordsearch.domain.jpa.User user, Collection<? extends GrantedAuthority> authorities) {
        this(user.getEmail(), user.getPassword(), authorities);
        
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.summoner = user.getSummoner();
    }
    
    public LoginUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
    
    public LoginUser(com.lolsearch.lolrecordsearch.domain.jpa.User user, boolean enabled, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        this(user.getEmail(), user.getPassword(), enabled, true, true, accountNonLocked, authorities);
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.summoner = user.getSummoner();
    }
    
    public LoginUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }
}
