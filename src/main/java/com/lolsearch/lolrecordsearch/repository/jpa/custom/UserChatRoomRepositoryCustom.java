package com.lolsearch.lolrecordsearch.repository.jpa.custom;

import com.lolsearch.lolrecordsearch.domain.jpa.UserChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserChatRoomRepositoryCustom {

    Page<UserChatRoom> findByUserId(Long userId, String title, Pageable pageable);
    
    Optional<UserChatRoom> findUserChatRoom(Long chatRoomId, Long userId);

}
