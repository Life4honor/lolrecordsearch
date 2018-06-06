package com.lolsearch.lolrecordsearch.domain.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "users")
public class User implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(unique = true)
    private String nickname;
    @Column(unique = true)
    private String summoner;
    @Column(name = "state_edit_date")
    private LocalDateTime stateEditDate = LocalDateTime.now();
    
    @JoinColumn(name = "user_state_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private UserState userState;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinTable(name = "users_roles"
            , joinColumns = @JoinColumn(name = "users_id"), inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private List<Role> roles = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Friend> friends = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PartyDetail> partyDetails = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserChatRoom> userChatRooms = new ArrayList<>();
    
    public void addRole(Role role) {
        if(!this.roles.contains(role)) {
            this.roles.add(role);
        }
    }
    
    public void addFriend(Friend friend) {
        if(!this.friends.contains(friend)) {
            this.friends.add(friend);
        }
        friend.setUser(this);
    }
    
    public void addPartyDetail(PartyDetail partyDetail) {
        if(!this.partyDetails.contains(partyDetail)) {
            this.partyDetails.add(partyDetail);
        }
        partyDetail.setUser(this);
    }
    
    public void addUserChatRoom(UserChatRoom userChatRoom) {
        if(!this.userChatRooms.contains(userChatRoom)) {
            this.userChatRooms.add(userChatRoom);
        }
        userChatRoom.setUser(this);
    }
    
}
