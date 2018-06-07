package com.lolsearch.lolrecordsearch.repository.jpa.custom;

import com.lolsearch.lolrecordsearch.domain.jpa.ChatRoom;
import com.lolsearch.lolrecordsearch.domain.jpa.QChatRoom;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

public class ChatRoomRepositoryImpl extends QuerydslRepositorySupport implements ChatRoomRepositoryCustom {
    
    private final QChatRoom qChatRoom = QChatRoom.chatRoom;
    
    public ChatRoomRepositoryImpl() {
        super(ChatRoom.class);
    }
    
    @Override
    public Page<ChatRoom> findChatRooms(Optional<String> title, Pageable pageable) {
    
        JPQLQuery<ChatRoom> query = from(qChatRoom);
        
        if(title.isPresent()) {
            query.where(qChatRoom.title.like("%" + title.get()+"%"));
        }
    
        List<ChatRoom> chatRooms = getQuerydsl().applyPagination(pageable, query).fetch();
    
        long count = query.fetchCount();
    
        return new PageImpl<>(chatRooms, pageable, count);
    }
}
