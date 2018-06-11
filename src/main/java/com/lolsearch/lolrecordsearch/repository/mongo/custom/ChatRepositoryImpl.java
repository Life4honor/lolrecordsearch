package com.lolsearch.lolrecordsearch.repository.mongo.custom;

import com.lolsearch.lolrecordsearch.domain.mongo.Chat;
import com.lolsearch.lolrecordsearch.dto.ChatMessage;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;

public class ChatRepositoryImpl implements ChatRepositoryCustom {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;
    
    @Override
    public long pushUserId(Long chatRoomId, Long userId) {
        Query query = Query.query(Criteria.where("chatRoomId").is(chatRoomId));
        Update update = new Update().addToSet("users").value(userId);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Chat.class);
        
        return updateResult.getModifiedCount();
    }
    
    @Override
    public Mono<UpdateResult> reactivePushUserId(Long chatRoomId, Long userId) {
        Query query = Query.query(Criteria.where("chatRoomId").is(chatRoomId));
        Update update = new Update().addToSet("users").value(userId);
        
        return reactiveMongoTemplate.updateFirst(query, update, Chat.class);
    }
    
    @Override
    public Chat pushChatMessage(Long chatRoomId, ChatMessage message) {
        Query query = Query.query(Criteria.where("chatRoomId").is(chatRoomId));
        Update update = new Update().push("chatMessages", message);
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions().returnNew(true);
        
        return mongoTemplate.findAndModify(query, update, findAndModifyOptions, Chat.class);
    }
    
    @Override
    public Mono<Chat> reactivePushChatMessage(Long chatRoomId, ChatMessage message) {
        Query query = Query.query(Criteria.where("chatRoomId").is(chatRoomId));
        Update update = new Update().push("chatMessages", message);
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions().returnNew(true);
        
        return reactiveMongoTemplate.findAndModify(query, update, findAndModifyOptions, Chat.class);
    }
    
    @Override
    public long pullChatUser(Long chatRoomId, Long userId) {
        Query query = Query.query(Criteria.where("chatRoomId").is(chatRoomId));
        Update update = new Update().pull("users", userId);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Chat.class);
        
        return updateResult.getModifiedCount();
    }
    
    @Override
    public Mono<UpdateResult> reactivePullChatUser(Long chatRoomId, Long userId) {
        Query query = Query.query(Criteria.where("chatRoomId").is(chatRoomId));
        Update update = new Update().pull("users", userId);
        
        return reactiveMongoTemplate.updateFirst(query, update, Chat.class);
    }
    
    @Override
    public Chat findByChatRoomIdWithChatMessageLimit(Long chatRoomId, int size) {
        
        Query query = Query.query(Criteria.where("chatRoomId").is(chatRoomId));
        
        query.fields().slice("chatMessages", size);
        
        return mongoTemplate.findOne(query, Chat.class);
    }
    
    @Override
    public Mono<Chat> reactiveFindByChatRoomIdWithChatMessageLimit(Long chatRoomId, int size) {
        Query query = Query.query(Criteria.where("chatRoomId").is(chatRoomId));
    
        query.fields().slice("chatMessages", size);
    
        return reactiveMongoTemplate.findOne(query, Chat.class);
    }
}
