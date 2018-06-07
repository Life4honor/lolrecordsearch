package com.lolsearch.lolrecordsearch.domain.mongo;

import com.lolsearch.lolrecordsearch.dto.ChatMessage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ToString
@Getter @Setter
@EqualsAndHashCode
@Document(collection = "chat")
public class Chat implements Serializable {
    
    @Id
    private String id;
    
    @Indexed(unique = true)
    private Long chatRoomId;
    private Set<Long> users = new HashSet<>();
    private List<ChatMessage> chatMessages = new ArrayList<>();
    
    public void addUser(Long userId) {
        this.users.add(userId);
    }
    
    public void addChatMessage(ChatMessage message) {
        this.chatMessages.add(message);
    }
    
}
