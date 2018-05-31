package com.lolsearch.lolrecordsearch.repository;

import com.lolsearch.lolrecordsearch.domain.UserState;
import com.lolsearch.lolrecordsearch.domain.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStateRepository extends JpaRepository<UserState, Integer> {

    UserState findByName(UserStatus status);

}
