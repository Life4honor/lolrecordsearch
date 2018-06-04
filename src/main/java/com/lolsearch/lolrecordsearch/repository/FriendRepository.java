package com.lolsearch.lolrecordsearch.repository;

import com.lolsearch.lolrecordsearch.domain.Friend;
import com.lolsearch.lolrecordsearch.repository.custom.FriendRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long>, FriendRepositoryCustom {
}
