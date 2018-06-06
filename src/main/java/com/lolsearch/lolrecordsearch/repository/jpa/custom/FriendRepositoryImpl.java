package com.lolsearch.lolrecordsearch.repository.jpa.custom;

import com.lolsearch.lolrecordsearch.domain.jpa.Friend;
import com.lolsearch.lolrecordsearch.domain.QFriend;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.List;

public class FriendRepositoryImpl extends QuerydslRepositorySupport implements FriendRepositoryCustom {
    
    private QFriend qFriend = QFriend.friend;
    
    @Autowired
    public FriendRepositoryImpl(EntityManager entityManager) {
        super(Friend.class);
        setEntityManager(entityManager);
    }
    
    @Override
    public Page<Friend> findFriendsByUserId(Long userId, String summoner, Pageable pageable) {
        
        JPQLQuery<Friend> query = from(qFriend).where(qFriend.user.id.eq(userId));
        
        if(summoner != null) {
            query.where(qFriend.summoner.like(summoner));
        }
    
        List<Friend> friends = getQuerydsl().applyPagination(pageable, query).fetch();
        
        long count = query.fetchCount();
        
        return new PageImpl<>(friends, pageable, count);
    }
}
