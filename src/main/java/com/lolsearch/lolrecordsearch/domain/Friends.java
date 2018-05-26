package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "friends")
public class Friends {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String summoner;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    public void setUsers(Users users){
        this.users = users;
        if(!users.getFriendsList().contains(this)){
            users.getFriendsList().add(this);
        }
    }


}
