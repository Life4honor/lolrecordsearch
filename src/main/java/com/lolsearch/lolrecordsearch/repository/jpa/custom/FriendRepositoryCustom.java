package com.lolsearch.lolrecordsearch.repository.jpa.custom;

import com.lolsearch.lolrecordsearch.domain.jpa.Friend;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FriendRepositoryCustom {
    
    Page<Friend> findFriendsByUserId(Long userId, String summoner, Pageable pageable);
    
}
