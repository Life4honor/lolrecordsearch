package com.lolsearch.lolrecordsearch.repository.mongo;

import com.lolsearch.lolrecordsearch.domain.mongo.Chat;
import com.lolsearch.lolrecordsearch.repository.mongo.custom.ChatRepositoryCustom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface ChatRepository extends ReactiveMongoRepository<Chat, String>, ChatRepositoryCustom {
    
    Mono<Chat> findByChatRoomId(Long chatRoomId);
    
    Mono<Void> deleteByChatRoomId(Long chatRoomId);
    
}
