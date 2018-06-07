package com.lolsearch.lolrecordsearch.repository.mongo;

import com.lolsearch.lolrecordsearch.domain.mongo.Chat;
import com.lolsearch.lolrecordsearch.repository.mongo.custom.ChatRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ChatRepository extends MongoRepository<Chat, String>, ChatRepositoryCustom {
    
    Optional<Chat> findByChatRoomId(Long chatRoomId);
    
    void deleteByChatRoomId(Long chatRoomId);
    
}
