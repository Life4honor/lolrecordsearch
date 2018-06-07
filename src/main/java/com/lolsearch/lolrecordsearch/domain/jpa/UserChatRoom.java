package com.lolsearch.lolrecordsearch.domain.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter
@Entity
@Table(name = "users_chat_rooms")
public class UserChatRoom implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "users_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "chat_rooms_id")
    private ChatRoom chatRoom;
    
    
    public void setUser(User user) {
        if(this.user != null) {
            this.user.getUserChatRooms().remove(this);
        }
        this.user = user;
        if(!user.getUserChatRooms().contains(this)) {
            user.getUserChatRooms().add(this);
        }
    }
    
    public void setChatRoom(ChatRoom chatRoom) {
        if(this.chatRoom != null) {
            this.chatRoom.getUserChatRooms().remove(this);
        }
        this.chatRoom = chatRoom;
        if(!chatRoom.getUserChatRooms().contains(this)) {
            chatRoom.getUserChatRooms().add(this);
        }
    }
    
    
}
