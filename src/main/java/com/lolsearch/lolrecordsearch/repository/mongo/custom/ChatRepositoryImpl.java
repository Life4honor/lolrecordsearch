package com.lolsearch.lolrecordsearch.repository.mongo.custom;

import com.lolsearch.lolrecordsearch.domain.mongo.Chat;
import com.lolsearch.lolrecordsearch.dto.ChatMessage;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class ChatRepositoryImpl implements ChatRepositoryCustom {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    @Override
    public Chat pushUserIdAndChatMessage(Long chatRoomId, Long userId, ChatMessage message) {
        Query query = Query.query(Criteria.where("chatRoomId").is(chatRoomId));
        Update update = new Update().push("users", userId).push("chatMessages", message);
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions().returnNew(true);
    
        return mongoTemplate.findAndModify(query, update, findAndModifyOptions, Chat.class);
    }
    
    @Override
    public long pullChatUser(Long chatRoomId, Long userId) {
        Query query = Query.query(Criteria.where("chatRoomId").is(chatRoomId));
        Update update = new Update().pull("users", userId);
        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Chat.class);
        
        return updateResult.getModifiedCount();
    }
    
    @Override
    public Chat findByChatRoomIdWithChatMessageLimit(Long chatRoomId, int size) {
        
        Query query = Query.query(Criteria.where("chatRoomId").is(chatRoomId));
        
        query.fields().slice("chatMessages", size);
    
        return mongoTemplate.findOne(query, Chat.class);
    }
}
