package com.lolsearch.lolrecordsearch.domain.redis;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@ToString
@EqualsAndHashCode
@Getter @Setter
@RedisHash("chat_room_users")
public class ChatRoomUser implements Serializable {

    @Id
    public String id;
    
    public Set<String> users = new HashSet<>();
    
    public void addUser(String userId) {
        users.add(userId);
    }

}
