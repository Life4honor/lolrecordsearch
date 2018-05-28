package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "user_state")
public class UserState implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private UserStatus name;
    
    @OneToMany(mappedBy = "userState", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();
    
    public void addUser(User user) {
        if(!this.users.contains(user)) {
            this.users.add(user);
        }
        user.setUserState(this);
    }
    
}
