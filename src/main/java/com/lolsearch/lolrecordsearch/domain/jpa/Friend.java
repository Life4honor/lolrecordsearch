package com.lolsearch.lolrecordsearch.domain.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter
@Entity
@Table(name = "friends")
public class Friend implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String summoner;
    
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;
    
    public void setUser(User user) {
        if(this.user != null) {
            this.user.getFriends().remove(this);
        }
        
        if(!user.getFriends().contains(this)) {
            user.getFriends().add(this);
        }
    }
    
}
