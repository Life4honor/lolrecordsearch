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
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

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
    public Optional<ChatRoom> findChatRoom(Long chatRoomId) {
        
        return chatRoomRepository.findById(chatRoomId);
    }
    
    @Override
    public Long createChatRoom(Long userId, String title) {
    
        User user = userRepository.findById(userId).get();
    
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setTitle(title);
    
        UserChatRoom userChatRoom = new UserChatRoom();
        userChatRoom.setUser(user);
        userChatRoom.setChatRoom(chatRoom);
        // RDB 저장
        UserChatRoom newUserChatRoom = userChatRoomRepository.save(userChatRoom);
    
        ChatRoom newChatRoom = newUserChatRoom.getChatRoom();
    
        Chat chat = new Chat();
        chat.setChatRoomId(newChatRoom.getId());
        // 몽고DB 저장
        chatRepository.save(chat).subscribe();
    
        return newChatRoom.getId();
    }
    
    @Override
    public long pushUserId(Long chatRoomId, Long userId) {
        
        return chatRepository.pushUserId(chatRoomId, userId);
    }
    
    @Override
    public Mono<UpdateResult> reactivePushUserId(Long chatRoomId, Long userId) {
        
        return chatRepository.reactivePushUserId(chatRoomId, userId);
    }
    
    @Override
    public Chat saveChatMessage(Long chatRoomId, ChatMessage chatMessage) {
        
        return chatRepository.pushChatMessage(chatRoomId, chatMessage);
    }
    
    @Override
    public Mono<Chat> reactiveSaveChatMessage(Long chatRoomId, ChatMessage chatMessage) {
        
        return chatRepository.reactivePushChatMessage(chatRoomId, chatMessage);
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<ChatMessage> findChatMessages(Long chatRoomId, int size) {
    
        Chat chat = chatRepository.findByChatRoomIdWithChatMessageLimit(chatRoomId, size);
        
        return chat.getChatMessages();
    }
    
    @Transactional(readOnly = true)
    @Override
    public Mono<Chat> reactiveFindChatMessages(Long chatRoomId, int size) {
    
        return chatRepository.reactiveFindByChatRoomIdWithChatMessageLimit(chatRoomId, size);
    }
    
    @Transactional(readOnly = true)
    @Override
    public Page<ChatRoom> findChatRooms(int page, String title) {
        
        if(StringUtils.isBlank(title)) {
            title = null;
        }
        
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page - 1, 10, sort);
        
        return chatRoomRepository.findChatRooms(Optional.ofNullable(title), pageable);
    }
    
    @Override
    public long deleteUserId(Long chatRoomId, Long userId) {
    
        return chatRepository.pullChatUser(chatRoomId, userId);
    }
    
    @Override
    public Mono<UpdateResult> reactiveDeleteUserId(Long chatRoomId, Long userId) {
        
        return chatRepository.reactivePullChatUser(chatRoomId, userId);
    }
    
    @Override
    public Optional<UserChatRoom> registUserChatRoom(Long chatRoomId, Long userId) {
    
        Optional<UserChatRoom> optionalUserChatRoom = userChatRoomRepository.findUserChatRoom(chatRoomId, userId);
        if(optionalUserChatRoom.isPresent()) {
            return Optional.empty();
        }
    
        User user = userRepository.findById(userId).get();
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).get();
    
        UserChatRoom userChatRoom = new UserChatRoom();
        userChatRoom.setUser(user);
        userChatRoom.setChatRoom(chatRoom);
    
        UserChatRoom save = userChatRoomRepository.save(userChatRoom);
    
        return Optional.of(save);
    }
}
