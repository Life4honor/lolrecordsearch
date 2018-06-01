package com.lolsearch.lolrecordsearch.repository.custom;

import com.lolsearch.lolrecordsearch.domain.User;
import com.lolsearch.lolrecordsearch.domain.UserState;

import java.util.Optional;

public interface UserRepositoryCustom {
    
    User findByEmail(String email);
    
    long countEmail(String email);
    
    long countNickname(String nickname);
    
    long countSummoner(String summoner);
    
    Optional<User> findUserByNickname(String nickname);
    
    Optional<User> findUserByEmailAndNickname(String email, String nickname);
    
    long updateUserNickname(Long id, String nickname);
    
    long updateUserSummoner(Long id, String summoner);
    
    long updateUserPassword(Long id, String password);
    
    long updateUserState(Long id, UserState userState);
    
}
