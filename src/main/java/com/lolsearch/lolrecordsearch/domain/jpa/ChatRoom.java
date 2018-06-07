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
@Table(name = "chat_rooms")
public class ChatRoom implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(name = "reg_date")
    private LocalDateTime regDate = LocalDateTime.now();
    
    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<UserChatRoom> userChatRooms = new ArrayList<>();
    
    public void addUserChatRoom(UserChatRoom userChatRoom) {
        if(!this.getUserChatRooms().contains(userChatRoom)) {
            this.getUserChatRooms().add(userChatRoom);
        }
        userChatRoom.setChatRoom(this);
    }
    
}
