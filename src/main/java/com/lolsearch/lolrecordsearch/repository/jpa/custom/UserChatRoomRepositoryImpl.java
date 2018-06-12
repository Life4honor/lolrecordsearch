package com.lolsearch.lolrecordsearch.repository.jpa.custom;

import com.lolsearch.lolrecordsearch.domain.jpa.QUserChatRoom;
import com.lolsearch.lolrecordsearch.domain.jpa.UserChatRoom;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;

@Slf4j
public class UserChatRoomRepositoryImpl extends QuerydslRepositorySupport implements UserChatRoomRepositoryCustom {
    
    private final QUserChatRoom qUserChatRoom = QUserChatRoom.userChatRoom;
    
    public UserChatRoomRepositoryImpl() {
        super(UserChatRoom.class);
    }
    
    @Override
    public Page<UserChatRoom> findByUserId(Long userId, String title, Pageable pageable) {
    
        JPQLQuery<UserChatRoom> query = from(qUserChatRoom)
                                        .where(qUserChatRoom.user.id.eq(userId))
                                        .innerJoin(qUserChatRoom.chatRoom).fetchJoin();
        
        if(StringUtils.isNotBlank(title)) {
            query.where(qUserChatRoom.chatRoom.title.like("%" + title + "%"));
        }
        
        List<UserChatRoom> userChatRooms = getQuerydsl().applyPagination(pageable, query).fetch();
        long totalCount = query.fetchCount();
        
        return new PageImpl<>(userChatRooms, pageable, totalCount);
    }
    
    @Override
    public Optional<UserChatRoom> findUserChatRoom(Long chatRoomId, Long userId) {
    
        JPQLQuery<UserChatRoom> query = from(qUserChatRoom)
                                        .where(
                                            qUserChatRoom.chatRoom.id.eq(chatRoomId)
                                            .and(qUserChatRoom.user.id.eq(userId))
                                        );
        
        return Optional.ofNullable(query.fetchOne());
    }
}
