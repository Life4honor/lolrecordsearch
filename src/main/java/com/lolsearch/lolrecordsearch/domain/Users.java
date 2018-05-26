package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "users")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    private String email;

    private String password;

    private String summoner;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "users")
    private List<UsersRoles> usersRolesList;

    public void addUserRole(UsersRoles usersRoles){
        usersRolesList.add(usersRoles);
        if(usersRoles.getUsers() != this){
            usersRoles.setUsers(this);
        }
    }

    @ManyToOne
    @JoinColumn(name = "user_state_id")
    private UserState userState;

    public void setUserState(UserState userState){
        this.userState = userState;
        if(!userState.getUsers().contains(this)){
            userState.getUsers().add(this);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "users")
    private List<Friends> friendsList;

    public void addFriends(Friends friends){
        this.friendsList.add(friends);
        if(friends.getUsers() != this){
            friends.setUsers(this);
        }
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "users")
    private List<Parties> partiesList;

    public void addParties(Parties parties){
        this.partiesList.add(parties);
        if(parties.getUsers() != this){
            parties.setUsers(this);
        }
    }
}
