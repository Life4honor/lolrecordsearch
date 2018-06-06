package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.UserState;
import com.lolsearch.lolrecordsearch.domain.jpa.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStateRepository extends JpaRepository<UserState, Integer> {

    UserState findByName(UserStatus status);

}
