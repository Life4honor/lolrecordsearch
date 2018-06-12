package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.UserChatRoom;
import com.lolsearch.lolrecordsearch.repository.jpa.custom.UserChatRoomRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long>, UserChatRoomRepositoryCustom {
    
    List<UserChatRoom> findByUserId(Long userId);
    
}
