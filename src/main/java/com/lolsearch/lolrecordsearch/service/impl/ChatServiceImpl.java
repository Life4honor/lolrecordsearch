package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.jpa.ChatRoom;
import com.lolsearch.lolrecordsearch.domain.jpa.User;
import com.lolsearch.lolrecordsearch.domain.jpa.UserChatRoom;
import com.lolsearch.lolrecordsearch.domain.mongo.Chat;
import com.lolsearch.lolrecordsearch.dto.ChatMessage;
import com.lolsearch.lolrecordsearch.repository.jpa.ChatRoomRepository;
import com.lolsearch.lolrecordsearch.repository.jpa.UserChatRoomRepository;
import com.lolsearch.lolrecordsearch.repository.jpa.UserRepository;
import com.lolsearch.lolrecordsearch.repository.mongo.ChatRepository;
import com.lolsearch.lolrecordsearch.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class ChatServiceImpl implements ChatService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    
    @Autowired
    private ChatRepository chatRepository;
    
    @Autowired
    private UserChatRoomRepository userChatRoomRepository;
    
    @Override
    public Long createChatRoom(Long userId, String title) {
    
        User user = userRepository.findById(userId).get();
    
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTitle(title);
    
        UserChatRoom userChatRoom = new UserChatRoom();
        userChatRoom.setUser(user);
        userChatRoom.setChatRoom(chatRoom);
    
        UserChatRoom newUserChatRoom = userChatRoomRepository.save(userChatRoom);
    
        ChatRoom newChatRoom = newUserChatRoom.getChatRoom();
    
        Chat chat = new Chat();
        chat.setChatRoomId(newChatRoom.getId());
    
        chatRepository.save(chat);
    
        return newChatRoom.getId();
    }
    
    @Override
    public Chat saveChatMessage(Long chatRoomId, Long userId, ChatMessage chatMessage) {
    
        return chatRepository.pushUserIdAndChatMessage(chatRoomId, userId, chatMessage);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<ChatMessage> findChatMessages(Long chatRoomId, int size) {
    
        Chat chat = chatRepository.findByChatRoomIdWithChatMessageLimit(chatRoomId, size);
        
        return chat.getChatMessages();
    }
}
