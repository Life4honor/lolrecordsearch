package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.UserChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserChatRoomRepository extends JpaRepository<UserChatRoom, Long> {
    
    List<UserChatRoom> findByUserId(Long userId);
    
}
